package edu.ucu.statisticbuilder.application.entitiesrankprocessing

import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serializer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer

class EntityCountSerde : Serde<EntityCount> {
    override fun serializer(): Serializer<EntityCount> {
        return JsonSerializer()
    }

    override fun deserializer(): Deserializer<EntityCount> {
        val deserializer: JsonDeserializer<EntityCount> = JsonDeserializer(EntityCount::class.java, false)
        deserializer.addTrustedPackages("*")
        return deserializer
    }
}
