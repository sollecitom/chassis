package org.sollecitom.chassis.pulsar.avro.serialization

import org.apache.avro.Schema
import org.apache.avro.generic.GenericDatumWriter
import org.apache.avro.generic.GenericRecord
import org.apache.avro.io.BinaryEncoder
import org.apache.avro.io.EncoderFactory
import org.apache.pulsar.client.api.SchemaSerializationException
import org.apache.pulsar.client.api.schema.SchemaWriter
import java.io.ByteArrayOutputStream

// copied from Pulsar and lightly modified (refactor)
internal class GenericAvroWriter(schema: Schema) : SchemaWriter<GenericRecord> {

    private val writer: GenericDatumWriter<GenericRecord>
    private val encoder: BinaryEncoder
    private val byteArrayOutputStream: ByteArrayOutputStream

    init {
        writer = GenericDatumWriter(schema)
        byteArrayOutputStream = ByteArrayOutputStream()
        encoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null)
    }

    @Synchronized
    override fun write(message: GenericRecord): ByteArray {
        return try {
            writer.write(message, encoder)
            encoder.flush()
            byteArrayOutputStream.toByteArray()
        } catch (e: Exception) {
            throw SchemaSerializationException(e)
        } finally {
            byteArrayOutputStream.reset()
        }
    }
}