package edu.ucu.languagedetector.infrastructure.evaluator

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan

@ConfigurationProperties(prefix = "evaluator")
@ConfigurationPropertiesScan
data class EvaluatorConfiguration(
    val host: String
) {}