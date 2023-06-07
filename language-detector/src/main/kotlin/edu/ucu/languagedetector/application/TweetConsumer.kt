package edu.ucu.languagedetector.application

import edu.ucu.languagedetector.domain.Tweet
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.net.URL
import java.net.URLEncoder
import java.net.http.HttpClient
import java.nio.charset.StandardCharsets
import kotlinx.serialization.json.*


@Component
class TweetConsumer {
    private val logger = KotlinLogging.logger {}
    val client = HttpClient.newBuilder().build()

    @KafkaListener(topics = ["tweets"])
    fun greetingListener(tweet: Tweet) {
        val response = URL("http://localhost:5000/language?text=" + URLEncoder.encode(tweet.text, StandardCharsets.UTF_8))
                .openStream()
                .bufferedReader()
                .use { it.readText() }

        val body: Map<String, JsonElement> = Json.parseToJsonElement(response).jsonObject

        val langauge = body["language"] ?: "ENGLISH"

        logger.info { langauge }
    }
}