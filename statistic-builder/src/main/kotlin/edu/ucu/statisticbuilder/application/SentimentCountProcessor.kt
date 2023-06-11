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
class SentimentCountProcessor {
    @Autowired
    fun buildPipeline(streamsBuilder: StreamsBuilder) {
        val input = streamsBuilder.stream(
            "sentimentdetectionresults",
            Consumed.with(Serdes.String(), JsonSerdes.sentimentDetectionResult()))

        val counts = input
            .mapValues { i -> i.sentiment }
            .groupBy(
                { key, sentiment -> sentiment },
                Grouped.with(Serdes.String(), Serdes.String())
            )
            .count()

        counts.toStream().to("sentimentcount")

        val table = Materialized.`as`<String, Long, KeyValueStore<Bytes, ByteArray>>("sentimentcounts")
            .withKeySerde(Serdes.String())
            .withValueSerde(Serdes.Long())

        streamsBuilder.globalTable("sentimentcount", table)
    }
}