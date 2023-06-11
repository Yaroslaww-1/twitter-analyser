package edu.ucu.statisticbuilder.domain

data class SentimentDetectionResult(
    val tweetId: Long,
    val sentiment: String) {
}