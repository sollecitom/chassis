package org.sollecitom.chassis.avro.serialization.utils

import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.avro.io.DecoderFactory
import org.apache.avro.io.EncoderFactory
import org.apache.avro.specific.SpecificDatumReader
import org.apache.avro.specific.SpecificDatumWriter
import java.io.ByteArrayOutputStream

object AvroSerializationUtils {

    fun writeAsBytes(record: GenericRecord, schema: Schema = record.schema): ByteArray {

        try {
            val writer = SpecificDatumWriter<GenericRecord>(schema)
            val output = ByteArrayOutputStream()
            val encoder = EncoderFactory.get().binaryEncoder(output, null)
            writer.write(record, encoder)
            encoder.flush()
            output.close()
            return output.toByteArray()
        } catch (e: NullPointerException) {
            error(e.message!!)
        }
    }

    fun readFromBytes(bytes: ByteArray, schema: Schema): GenericRecord {

        val reader = SpecificDatumReader<GenericRecord>(schema)
        val decoder = DecoderFactory.get().binaryDecoder(bytes, null)
        return reader.read(null, decoder)
    }
}