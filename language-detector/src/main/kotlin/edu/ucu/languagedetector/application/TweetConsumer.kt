package edu.ucu.languagedetector.application

import edu.ucu.languagedetector.domain.Tweet
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component


@Component
class TweetConsumer {
    private val logger = KotlinLogging.logger {}

    @KafkaListener(topics = ["tweets"])
    fun greetingListener(tweet: Tweet) {
        logger.info { tweet }
        // process greeting message
    }
}