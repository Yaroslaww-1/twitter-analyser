package edu.ucu.statisticbuilder.application.entitiesrankprocessing

import edu.ucu.statisticbuilder.infrastructure.kafka.JsonSerdes
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Grouped
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.state.KeyValueStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class EntitiesRankProcessor {
    @Autowired
    fun buildPipeline(streamsBuilder: StreamsBuilder) {
        val input = streamsBuilder.stream(
            "entitydetectionresults",
            Consumed.with(Serdes.String(), JsonSerdes.entityDetectionResult()))

        val counts = input
            .mapValues { i -> i.entity }
            .groupBy(
                { key, entity -> entity },
                Grouped.with(Serdes.String(), Serdes.String())
            )
            .count()

        counts
            .groupBy(
                { entity, count -> KeyValue.pair("all", EntityCount(entity, count)) },
                Grouped.with(Serdes.String(), EntityCountSerde())
            )
            .aggregate(
                { TopTenEntities() },
                { key, value, aggregate ->
                    aggregate.add(value)
                    aggregate
                },
                { key, value, aggregate ->
                    aggregate.remove(value)
                    aggregate
                },
                Materialized.`as`<String, TopTenEntities, KeyValueStore<Bytes, ByteArray>>("entitycount-localtable")
                    .withKeySerde(Serdes.String())
                    .withValueSerde(TopTenEntitiesSerde())
            )
            .toStream()
            .to("entitycount")

        val table = Materialized.`as`<String, TopTenEntities, KeyValueStore<Bytes, ByteArray>>("entitycounts")
            .withKeySerde(Serdes.String())
            .withValueSerde(TopTenEntitiesSerde())

        streamsBuilder.globalTable("entitycount", table)
    }
}