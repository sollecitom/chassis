package org.sollecitom.chassis.pulsar.avro.serialization

import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.pulsar.client.api.schema.SchemaReader
import com.element.dpg.libs.chassis.avro.serialization.utils.RecordDeserializer
import java.io.InputStream

internal class AvroReader<VALUE>(originalAvroSchema: Schema, private val useProvidedSchemaAsReaderSchema: Boolean = true, private val deserialize: (GenericRecord) -> VALUE) : SchemaReader<VALUE> {

    constructor(deserializer: RecordDeserializer<VALUE>, useProvidedSchemaAsReaderSchema: Boolean = true) : this(deserializer.schema, useProvidedSchemaAsReaderSchema, deserializer::deserialize)

    private val reader = MultiVersionGenericAvroReader(useProvidedSchemaAsReaderSchema, originalAvroSchema)

    override fun read(bytes: ByteArray, offset: Int, length: Int): VALUE {

        val record = reader.read(bytes, offset, length)
        return deserialize(record)
    }

    override fun read(inputStream: InputStream): VALUE {

        val record = reader.read(inputStream)
        return deserialize(record)
    }
}