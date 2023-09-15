package org.sollecitom.chassis.pulsar.avro.serialization

import org.apache.avro.Schema
import org.apache.avro.generic.GenericDatumReader
import org.apache.avro.generic.GenericRecord
import org.apache.avro.io.Decoder
import org.apache.avro.io.DecoderFactory
import org.apache.avro.io.EncoderFactory
import org.apache.pulsar.client.api.SchemaSerializationException
import org.apache.pulsar.client.api.schema.Field
import org.apache.pulsar.client.api.schema.SchemaReader
import org.apache.pulsar.client.impl.schema.generic.GenericAvroRecord
import org.apache.pulsar.client.impl.schema.generic.GenericAvroSchema
import org.sollecitom.chassis.logger.core.loggable.Loggable
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

// copied from Pulsar and lightly modified (refactor)
internal class GenericAvroReader(writerSchema: Schema?, private val schema: Schema, private val schemaVersion: ByteArray?) : SchemaReader<GenericRecord> {

    constructor(schema: Schema) : this(null, schema, null)

    private val byteArrayOutputStream = ByteArrayOutputStream()
    private val encoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null)
    private val fields: List<Field> = schema.fields.map { f: Schema.Field -> Field(f.name(), f.pos()) }
    private var reader: GenericDatumReader<GenericAvroRecord> = if (writerSchema == null) GenericDatumReader(schema) else GenericDatumReader(writerSchema, schema)
    private var offset: Int = if (schema.getObjectProp(GenericAvroSchema.OFFSET_PROP) != null) schema.getObjectProp(GenericAvroSchema.OFFSET_PROP).toString().toInt() else 0

    override fun read(bytes: ByteArray, offset: Int, length: Int): GenericRecord {
        var internalOffset = offset
        return try {
            if (internalOffset == 0 && this.offset > 0) {
                internalOffset = this.offset
            }
            val decoder: Decoder = DecoderFactory.get().binaryDecoder(bytes, internalOffset, length - internalOffset, null)
            reader.read(null, decoder) as GenericRecord
//            GenericAvroRecord(schemaVersion, schema, fields, avroRecord)
        } catch (e: IOException) {
            throw SchemaSerializationException(e)
        } catch (e: IndexOutOfBoundsException) {
            throw SchemaSerializationException(e)
        }
    }

    override fun read(inputStream: InputStream): GenericRecord {
        return try {
            val decoder: Decoder = DecoderFactory.get().binaryDecoder(inputStream, null)
            reader.read(null, decoder) as GenericRecord
//            GenericAvroRecord(schemaVersion, schema, fields, avroRecord)
        } catch (e: IOException) {
            throw SchemaSerializationException(e)
        } catch (e: IndexOutOfBoundsException) {
            throw SchemaSerializationException(e)
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                logger.error(e) { "GenericAvroReader close inputStream close error" }
            }
        }
    }

    override fun getNativeSchema(): Optional<Any> = Optional.of(schema)

    companion object : Loggable()
}