package edu.ucu.languagedetector.infrastructure.kafka

import edu.ucu.languagedetector.domain.Tweet
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.core.DefaultKafkaConsumerFactory


@EnableKafka
@Configuration
class KafkaConsumerConfiguration {
    @Value(value = "\${spring.kafka.bootstrap-servers}")
    private val bootstrapAddress: String = ""

    @Bean
    fun consumerFactory(): ConsumerFactory<String, Tweet> {
        val deserializer = JsonDeserializer(Tweet::class.java, false)
        deserializer.addTrustedPackages("*")

        return DefaultKafkaConsumerFactory(
            mapOf<String, Any>(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
                ConsumerConfig.GROUP_ID_CONFIG to "languagedetector",
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest"
            ),
            StringDeserializer(),
            deserializer)
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Tweet> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Tweet>()
        factory.consumerFactory = consumerFactory()
        return factory
    }
}