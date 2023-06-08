package edu.ucu.entitydetector.application

import edu.ucu.entitydetector.domain.EntityDetectionResult
import edu.ucu.entitydetector.domain.Tweet
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.kafka.core.KafkaTemplate


@Component
class TweetConsumer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    private val logger = KotlinLogging.logger {}

    @KafkaListener(topics = ["tweets"])
    fun tweetsListener(tweets: List<Tweet>) {
        logger.info { "Started batch detection" }

        for (tweet in tweets) {
            val entities = "(?<=@)\\w+".toRegex().findAll(tweet.text).map { it.value }.toList()
            for (entity in entities) {
                kafkaTemplate.send("entitydetectionresults", EntityDetectionResult(tweet.id, entity))
            }
        }

        logger.info { "Finished batch detection" }
    }
}