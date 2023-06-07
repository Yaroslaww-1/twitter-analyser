package edu.ucu.languagedetector

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LanguageDetectorApplication

fun main(args: Array<String>) {
	runApplication<LanguageDetectorApplication>(*args)
}
