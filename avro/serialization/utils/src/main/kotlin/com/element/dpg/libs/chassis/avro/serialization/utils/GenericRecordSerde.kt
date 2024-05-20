package com.element.dpg.libs.chassis.avro.serialization.utils

import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord

class GenericRecordSerde(override val schema: Schema) : RecordSerde<GenericRecord> {

    override fun serialize(value: GenericRecord) = value

    override fun deserialize(record: GenericRecord) = record
}