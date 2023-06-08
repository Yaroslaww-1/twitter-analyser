package edu.ucu.sentimentdetector.application

import edu.ucu.sentimentdetector.domain.SentimentDetectionResult
import edu.ucu.sentimentdetector.domain.Tweet
import kotlinx.serialization.encodeToString
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import kotlinx.serialization.json.*
import org.springframework.kafka.core.KafkaTemplate
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


@Component
class TweetConsumer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    private val logger = KotlinLogging.logger {}
    private val client = HttpClient.newBuilder().build()

    @KafkaListener(topics = ["tweets"])
    fun tweetsListener(tweets: List<Tweet>) {
        logger.info { "Started batch detection" }
        val request = tweets.associate { it.id to it.text }

        val req = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:5000/sentiment"))
            .POST(HttpRequest.BodyPublishers.ofString(Json.encodeToString(request)))
            .build()
        val response = client.send(req, HttpResponse.BodyHandlers.ofString())

        val body: Map<String, JsonElement> = Json.parseToJsonElement(response.body()).jsonObject

        val detectionResults = body.entries.map { SentimentDetectionResult(it.key.toLong(), it.value.toString()) }

        for (detectionResult in detectionResults) {
            kafkaTemplate.send("sentimentdetectionresults", detectionResult)
        }
        logger.info { "Finished batch detection" }
    }
}