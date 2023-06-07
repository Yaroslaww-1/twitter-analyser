package edu.ucu.languagedetector.domain

data class LanguageDetectionResult(
    val tweetId: Long,
    val language: String) {
}