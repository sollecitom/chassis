package org.sollecitom.chassis.pulsar.avro.serialization

import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.pulsar.client.api.schema.SchemaReader
import org.apache.pulsar.client.impl.schema.SchemaUtils
import org.apache.pulsar.client.impl.schema.generic.GenericAvroSchema
import org.apache.pulsar.client.impl.schema.util.SchemaUtil
import org.apache.pulsar.common.protocol.schema.BytesSchemaVersion
import org.sollecitom.chassis.logger.core.loggable.Loggable

// copied from Pulsar and lightly modified (refactor)
internal class MultiVersionGenericAvroReader(useProvidedSchemaAsReaderSchema: Boolean, readerSchema: Schema) : AbstractMultiVersionGenericReader(useProvidedSchemaAsReaderSchema, GenericAvroReader(readerSchema), readerSchema) {

    override fun loadReader(schemaVersion: BytesSchemaVersion): SchemaReader<GenericRecord> {

        val schemaInfo = getSchemaInfoByVersion(schemaVersion.get())
        return if (schemaInfo != null) {
            logger.info { "Load schema reader for version(${SchemaUtils.getStringSchemaVersion(schemaVersion.get())}), schema is : $schemaInfo" }
            val writerSchema = SchemaUtil.parseAvroSchema(schemaInfo.schemaDefinition)
            val readerSchema = if (useProvidedSchemaAsReaderSchema) readerSchema else writerSchema
            readerSchema.addProp(GenericAvroSchema.OFFSET_PROP, schemaInfo.properties.getOrDefault(GenericAvroSchema.OFFSET_PROP, "0"))
            GenericAvroReader(writerSchema, readerSchema, schemaVersion.get())
        } else {
            logger.warn { "No schema found for version(${SchemaUtils.getStringSchemaVersion(schemaVersion.get())}), use latest schema : $readerSchema" }
            providerSchemaReader
        }
    }

    companion object : Loggable()
}