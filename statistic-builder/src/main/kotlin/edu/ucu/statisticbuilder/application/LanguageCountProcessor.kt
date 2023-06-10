package edu.ucu.statisticbuilder.application

import edu.ucu.statisticbuilder.infrastructure.kafka.JsonSerdes
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Grouped
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.state.KeyValueStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class LanguageCountProcessor {
    @Autowired
    fun buildPipeline(streamsBuilder: StreamsBuilder) {
        val input = streamsBuilder.stream(
            "languagedetectionresults",
            Consumed.with(Serdes.String(), JsonSerdes.languageDetectionResult()))

        val counts = input
            .mapValues { i -> i.language }
            .groupBy(
                { key, language -> language },
                Grouped.with(Serdes.String(), Serdes.String())
            )
            .count()

        counts.toStream().to("languagecount")

        val table = Materialized.`as`<String, Long, KeyValueStore<Bytes, ByteArray>>("languagecounts")
            .withKeySerde(Serdes.String())
            .withValueSerde(Serdes.Long())

        streamsBuilder.globalTable("languagecount", table)
    }
}