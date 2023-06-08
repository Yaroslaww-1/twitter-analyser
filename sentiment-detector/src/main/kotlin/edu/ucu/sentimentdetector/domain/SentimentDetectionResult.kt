package edu.ucu.sentimentdetector.domain

data class SentimentDetectionResult(
    val tweetId: Long,
    val sentiment: String) {
}