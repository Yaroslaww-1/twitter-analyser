package edu.ucu.producer.infrastructure.kafka

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.serializer.JsonSerializer


@Configuration
class TopicConfiguration {
    @Value(value = "\${spring.kafka.bootstrap-servers}")
    private val bootstrapAddress: String = ""

    @Bean
    fun admin(): KafkaAdmin {
        return KafkaAdmin(mapOf(
            AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress
        ))
    }

    @Bean
    fun tweets(): NewTopic {
        return TopicBuilder.name("tweets")
            .partitions(1)
            .replicas(1)
            .build()
    }

    @Bean
    fun objectTemplate(): KafkaTemplate<String, Any> {
        return KafkaTemplate<String, Any>(
            DefaultKafkaProducerFactory(
                mapOf(
                    AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
                    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
                )
            ))
    }
}