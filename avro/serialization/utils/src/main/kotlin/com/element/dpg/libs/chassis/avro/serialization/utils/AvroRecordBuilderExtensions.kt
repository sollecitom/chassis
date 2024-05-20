package com.element.dpg.libs.chassis.avro.serialization.utils

fun <T : Any> RecordBuilder.setAsRecord(fieldName: String, value: T, serializer: RecordSerializer<T>) = setAsRecordOrNull(fieldName, value, serializer)

fun <T : Any> RecordBuilder.setAsRecordOrNull(fieldName: String, value: T?, serializer: RecordSerializer<T>) = value?.let { set(fieldName, serializer.serialize(it)) }