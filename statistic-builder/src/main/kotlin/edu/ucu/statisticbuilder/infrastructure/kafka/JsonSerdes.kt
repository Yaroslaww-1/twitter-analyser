package edu.ucu.statisticbuilder.infrastructure.kafka

import edu.ucu.statisticbuilder.domain.LanguageDetectionResult
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
    }
}