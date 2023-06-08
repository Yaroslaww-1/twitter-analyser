package edu.ucu.sentimentdetector

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SentimentDetectorApplication

fun main(args: Array<String>) {
    runApplication<SentimentDetectorApplication>(*args)
}
