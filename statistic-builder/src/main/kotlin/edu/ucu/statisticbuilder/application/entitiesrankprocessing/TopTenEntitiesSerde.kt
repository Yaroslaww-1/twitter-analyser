package edu.ucu.statisticbuilder.application.entitiesrankprocessing

import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serializer
import java.io.*

class TopTenEntitiesSerde : Serde<TopTenEntities> {
    override fun configure(map: Map<String?, *>?, b: Boolean) {}
    override fun close() {}
    override fun serializer(): Serializer<TopTenEntities> {
        return object : Serializer<TopTenEntities> {
            override fun configure(map: Map<String?, *>?, b: Boolean) {}
            override fun serialize(s: String?, topEntities: TopTenEntities): ByteArray {
                val out = ByteArrayOutputStream()
                val dataOutputStream = DataOutputStream(out)
                try {
                    for (entityCount in topEntities) {
                        dataOutputStream.writeInt(entityCount.entity.length)
                        dataOutputStream.writeChars(entityCount.entity)
                        dataOutputStream.writeLong(entityCount.count)
                    }
                    dataOutputStream.flush()
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
                return out.toByteArray()
            }

            override fun close() {}
        }
    }

    override fun deserializer(): Deserializer<TopTenEntities> {
        return object : Deserializer<TopTenEntities> {
            override fun configure(map: Map<String?, *>?, b: Boolean) {}
            override fun deserialize(s: String?, bytes: ByteArray?): TopTenEntities? {
                if (bytes == null || bytes.size == 0) {
                    return null
                }
                val result = TopTenEntities()
                val dataInputStream = DataInputStream(ByteArrayInputStream(bytes))
                try {
                    while (dataInputStream.available() > 0) {
                        val length = dataInputStream.readInt()
                        var entity = ""
                        for (i in (1..length)) {
                            entity += dataInputStream.readChar()
                        }
                        val count = dataInputStream.readLong()
                        result.add(
                            EntityCount(entity, count)
                        )
                    }
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
                return result
            }

            override fun close() {}
        }
    }
}