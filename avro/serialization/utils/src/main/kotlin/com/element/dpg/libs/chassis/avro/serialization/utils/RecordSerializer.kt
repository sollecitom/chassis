package com.element.dpg.libs.chassis.avro.serialization.utils

import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord

interface RecordSerializer<in VALUE> {

    val schema: Schema

    fun serialize(value: VALUE): GenericRecord

    fun writeAsBytes(record: GenericRecord): ByteArray = AvroSerializationUtils.writeAsBytes(record)

    fun serializeToBytes(value: VALUE): ByteArray = serialize(value).let(::writeAsBytes)

    fun buildRecord(customize: RecordBuilder.() -> Unit): GenericRecord = RecordBuilder(schema).apply(customize).build()
}
