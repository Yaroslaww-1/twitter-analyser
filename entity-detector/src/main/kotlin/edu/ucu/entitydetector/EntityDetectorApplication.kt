package edu.ucu.entitydetector

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EntityDetectorApplication

fun main(args: Array<String>) {
    runApplication<EntityDetectorApplication>(*args)
}
