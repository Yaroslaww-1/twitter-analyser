package edu.ucu.entitydetector.domain

import java.time.Instant

data class Tweet(
    val id: Long,
    val date: Instant,
    val user: String,
    val text: String) {
}