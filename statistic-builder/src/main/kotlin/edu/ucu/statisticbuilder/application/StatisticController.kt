package edu.ucu.statisticbuilder.application

import org.apache.kafka.streams.StoreQueryParameters
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StatisticController(
    private val factoryBean: StreamsBuilderFactoryBean
) {
    @GetMapping("/languages")
    fun getLanguages(): Map<String, Long> {
        val result = mutableMapOf<String, Long>()

        factoryBean.kafkaStreams
            ?.store(StoreQueryParameters.fromNameAndType("languagecounts", QueryableStoreTypes.keyValueStore<String, Long>()))
            ?.all()
            ?.forEach { keyValue -> result[keyValue.key] = keyValue.value }

        return result
    }
}