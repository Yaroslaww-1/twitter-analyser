//package edu.ucu.statisticbuilder.application
//
//import edu.ucu.statisticbuilder.infrastructure.kafka.JsonSerdes
//import org.apache.kafka.common.serialization.Serdes
//import org.apache.kafka.streams.Topology
//
//class Topology {
//    val topology: Topology = Topology()
//
//    init {
//        topology.addSource(
//            "Language Detection Results",
//            Serdes.String().deserializer(),
//            JsonSerdes.languageDetectionResult().deserializer(),
//            "languagedetectionresults"
//        )
//    }
//}