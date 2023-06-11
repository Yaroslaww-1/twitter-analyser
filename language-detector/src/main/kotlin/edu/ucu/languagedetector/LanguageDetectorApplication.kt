package edu.ucu.languagedetector

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("edu.ucu.languagedetector.infrastructure")
class LanguageDetectorApplication

fun main(args: Array<String>) {
	runApplication<LanguageDetectorApplication>(*args)
}
