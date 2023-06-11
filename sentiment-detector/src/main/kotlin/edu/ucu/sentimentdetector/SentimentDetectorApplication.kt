package edu.ucu.sentimentdetector

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("edu.ucu.sentimentdetector.infrastructure")
class SentimentDetectorApplication

fun main(args: Array<String>) {
    runApplication<SentimentDetectorApplication>(*args)
}
