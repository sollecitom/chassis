package com.element.dpg.libs.chassis.pulsar.json.serialization

import com.element.dpg.libs.chassis.json.utils.serde.JsonDeserializer
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerializer
import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.api.schema.SchemaReader
import org.apache.pulsar.client.api.schema.SchemaWriter
import org.apache.pulsar.client.impl.schema.AbstractStructSchema
import org.apache.pulsar.common.schema.SchemaInfo
import org.json.JSONObject
import java.io.InputStream

fun <VALUE : Any> JsonSerde.SchemaAware<VALUE>.asPulsarSchema(): Schema<VALUE> = PulsarSchemas.forSerde(this)

private object PulsarSchemas {

    fun <VALUE : Any> forSerde(serde: JsonSerde.SchemaAware<VALUE>): Schema<VALUE> = createSchema(serde)

    private fun <VALUE : Any> createSchema(serde: JsonSerde.SchemaAware<VALUE>): Schema<VALUE> {

        return JsonSchema(serde)
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

    private class JsonSchema<VALUE : Any>(private val serde: JsonSerde.SchemaAware<VALUE>) : AbstractStructSchema<VALUE>(serde.schemaInfo()) {

        private val jsonReader = JsonReader(serde)
        private val jsonWriter = JsonWriter(serde)

        init {
            setReader(jsonReader)
            setWriter(jsonWriter)
        }
    }

//    private fun <VALUE : Any> JsonSerde.SchemaAware<VALUE>.schemaDefinition(): SchemaDefinition<VALUE> {
//
//        val writer = JsonWriter(this)
//        val reader = JsonReader(this)
//        return SchemaDefinition.builder<VALUE>().withSchemaWriter(writer).withSchemaReader(reader).withJsonDef(schema.source.toString()).build()
//    }

    private fun <VALUE : Any> JsonSerde.SchemaAware<VALUE>.schemaInfo(): SchemaInfo = Schema.STRING.schemaInfo // SchemaUtil.parseSchemaInfo(schemaDefinition(), SchemaType.JSON)
}