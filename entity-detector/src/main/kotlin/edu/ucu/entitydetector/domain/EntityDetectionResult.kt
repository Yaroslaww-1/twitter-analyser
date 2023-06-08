package edu.ucu.entitydetector.domain

data class EntityDetectionResult(
    val tweetId: Long,
    val entity: String) {
}