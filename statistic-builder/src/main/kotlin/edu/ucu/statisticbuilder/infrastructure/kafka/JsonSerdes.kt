package edu.ucu.statisticbuilder.infrastructure.kafka

import edu.ucu.statisticbuilder.domain.EntityDetectionResult
import edu.ucu.statisticbuilder.domain.LanguageDetectionResult
import edu.ucu.statisticbuilder.domain.SentimentDetectionResult
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer


class JsonSerdes {
    companion object {
        fun languageDetectionResult(): Serde<LanguageDetectionResult> {
            val serializer: JsonSerializer<LanguageDetectionResult> = JsonSerializer()
            val deserializer: JsonDeserializer<LanguageDetectionResult> = JsonDeserializer(LanguageDetectionResult::class.java, false)
            deserializer.addTrustedPackages("*")
            return Serdes.serdeFrom(serializer, deserializer)
        }

        fun sentimentDetectionResult(): Serde<SentimentDetectionResult> {
            val serializer: JsonSerializer<SentimentDetectionResult> = JsonSerializer()
            val deserializer: JsonDeserializer<SentimentDetectionResult> = JsonDeserializer(SentimentDetectionResult::class.java, false)
            deserializer.addTrustedPackages("*")
            return Serdes.serdeFrom(serializer, deserializer)
        }

        fun entityDetectionResult(): Serde<EntityDetectionResult> {
            val serializer: JsonSerializer<EntityDetectionResult> = JsonSerializer()
            val deserializer: JsonDeserializer<EntityDetectionResult> = JsonDeserializer(EntityDetectionResult::class.java, false)
            deserializer.addTrustedPackages("*")
            return Serdes.serdeFrom(serializer, deserializer)
        }
    }
}