package edu.ucu.languagedetector.application

import edu.ucu.languagedetector.domain.LanguageDetectionResult
import edu.ucu.languagedetector.domain.Tweet
import edu.ucu.languagedetector.infrastructure.evaluator.EvaluatorConfiguration
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
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val evaluatorConfiguration: EvaluatorConfiguration
) {
    private val logger = KotlinLogging.logger {}
    private val client = HttpClient.newBuilder().build()

    @KafkaListener(topics = ["tweets"])
    fun tweetsListener(tweets: List<Tweet>) {
        logger.info { "Started batch detection" }
        val request = tweets.associate { it.id to it.text }

        val req = HttpRequest.newBuilder()
            .uri(URI.create("${evaluatorConfiguration.host}/languages"))
            .POST(HttpRequest.BodyPublishers.ofString(Json.encodeToString(request)))
            .build()
        val response = client.send(req, HttpResponse.BodyHandlers.ofString())

        val body: Map<String, JsonElement> = Json.parseToJsonElement(response.body()).jsonObject

        val detectionResults = body.entries.map { LanguageDetectionResult(it.key.toLong(), it.value.jsonPrimitive.content) }

        for (detectionResult in detectionResults) {
            kafkaTemplate.send("languagedetectionresults", detectionResult)
        }
        logger.info { "Finished batch detection" }
    }
}