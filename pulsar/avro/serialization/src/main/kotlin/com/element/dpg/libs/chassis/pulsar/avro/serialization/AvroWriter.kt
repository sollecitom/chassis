package com.element.dpg.libs.chassis.pulsar.avro.serialization

import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.pulsar.client.api.schema.SchemaWriter
import com.element.dpg.libs.chassis.avro.serialization.utils.RecordSerializer

internal class AvroWriter<VALUE>(originalAvroSchema: Schema, private val serialize: (VALUE) -> GenericRecord) : SchemaWriter<VALUE> {

    constructor(serializer: RecordSerializer<VALUE>) : this(serializer.schema, serializer::serialize)

    private val writer = GenericAvroWriter(originalAvroSchema)

    override fun write(message: VALUE): ByteArray {

        val record = serialize(message)
        return writer.write(record)
    }
}