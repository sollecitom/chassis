package com.element.dpg.libs.chassis.avro.serialization.utils

import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord

interface RecordDeserializer<out VALUE> {

    val schema: Schema

    fun deserialize(record: GenericRecord): VALUE

    fun deserializeFromBytes(bytes: ByteArray): VALUE = readFromBytes(bytes).let(::deserialize)

    fun readFromBytes(bytes: ByteArray): GenericRecord = AvroSerializationUtils.readFromBytes(bytes, schema)
}