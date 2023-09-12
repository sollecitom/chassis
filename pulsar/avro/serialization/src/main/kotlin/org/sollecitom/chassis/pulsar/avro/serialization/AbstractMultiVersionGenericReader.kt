package org.sollecitom.chassis.pulsar.avro.serialization

import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.pulsar.client.api.schema.SchemaReader
import org.apache.pulsar.client.impl.schema.reader.AbstractMultiVersionAvroBaseReader

internal abstract class AbstractMultiVersionGenericReader protected constructor(protected val useProvidedSchemaAsReaderSchema: Boolean, providerSchemaReader: SchemaReader<GenericRecord>, readerSchema: Schema) : AbstractMultiVersionAvroBaseReader<GenericRecord>(providerSchemaReader, readerSchema)