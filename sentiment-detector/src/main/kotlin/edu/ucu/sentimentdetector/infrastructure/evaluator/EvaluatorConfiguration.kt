package edu.ucu.sentimentdetector.infrastructure.evaluator

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan

@ConfigurationProperties(prefix = "evaluator")
@ConfigurationPropertiesScan
data class EvaluatorConfiguration(
    val host: String
) {}