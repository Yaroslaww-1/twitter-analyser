package edu.ucu.producer.application

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import edu.ucu.producer.domain.Tweet
import mu.KotlinLogging
import org.springframework.core.io.ClassPathResource
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


@Component
class TweetReader(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    private val logger = KotlinLogging.logger {}

    private var tweets: List<Tweet> = emptyList()
    private var offset = 0

    init {
        val file = ClassPathResource("tweets.csv").inputStream
        val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss 'PDT' yyyy")

        val tweets = csvReader().readAll(file).map { t ->
            Tweet(
                t[1].toLong(),
                LocalDateTime.parse(t[2], formatter).toInstant(ZoneOffset.UTC),
                t[4],
                t[5])
        }
        this.tweets = tweets.shuffled()
    }

    @Scheduled(fixedRate = 5000)
    fun reportCurrentTime() {
        for (i in (offset..offset + 100)) {
            if (tweets.size == i) break

            kafkaTemplate.send("tweets", tweets[i])
            offset++
        }

        logger.info { "Batch ${offset / 100 + 1} successfully sent." }
    }
}