package org.sollecitom.chassis.pulsar.json.serialization

import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.api.schema.SchemaDefinition
import org.apache.pulsar.client.api.schema.SchemaReader
import org.apache.pulsar.client.api.schema.SchemaWriter
import org.apache.pulsar.client.impl.schema.AvroSchema
import org.json.JSONObject
import org.sollecitom.chassis.json.utils.serde.JsonDeserializer
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.JsonSerializer
import java.io.InputStream

fun <VALUE : Any> JsonSerde.SchemaAware<VALUE>.pulsarAvroSchema(): AvroSchema<VALUE> = PulsarSchemas.forSerde(this)

private object PulsarSchemas {

    fun <VALUE : Any> forSerde(serde: JsonSerde.SchemaAware<VALUE>): AvroSchema<VALUE> = createSchema(serde)

    private fun <VALUE : Any> createSchema(serde: JsonSerde.SchemaAware<VALUE>): AvroSchema<VALUE> {

        val writer = JsonWriter(serde)
        val reader = JsonReader(serde)
        val definition = SchemaDefinition.builder<VALUE>().withSchemaWriter(writer).withSchemaReader(reader).withJsonDef(serde.schema.toString()).build()
        return Schema.AVRO(definition) as AvroSchema<VALUE>
    }

    private class JsonWriter<VALUE : Any>(private val serializer: JsonSerializer<VALUE>) : SchemaWriter<VALUE> {

        override fun write(message: VALUE): ByteArray {

            val json = serializer.serialize(message)
            return json.toString().encodeToByteArray()
        }
    }

    private class JsonReader<VALUE : Any>(private val deserializer: JsonDeserializer<VALUE>) : SchemaReader<VALUE> {

        override fun read(bytes: ByteArray, offset: Int, length: Int): VALUE {

            val asString = String(bytes, offset, length)
            val json = JSONObject(asString)
            return deserializer.deserialize(json)
        }

        override fun read(inputStream: InputStream): VALUE {

            val bytes = inputStream.readAllBytes()
            return read(bytes, 0, bytes.size)
        }
    }
}