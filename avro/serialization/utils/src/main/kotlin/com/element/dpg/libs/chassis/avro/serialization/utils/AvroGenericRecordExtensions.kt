@file:Suppress("UNCHECKED_CAST")

package com.element.dpg.libs.chassis.avro.serialization.utils

import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.util.*

fun GenericRecord.getOrNull(key: String): Any? = get(key)

fun GenericRecord.getStringOrNull(key: String): String? = get(key)?.asString()
fun GenericRecord.getString(key: String) = getStringOrNull(key)!!

fun GenericRecord.getIntOrNull(key: String): Int? = get(key)?.let { it as Int }
fun GenericRecord.getInt(key: String) = getIntOrNull(key)!!

fun GenericRecord.getBigIntegerOrNull(key: String): BigInteger? = get(key)?.let { it as BigInteger }
fun GenericRecord.getBigInteger(key: String) = getBigIntegerOrNull(key)!!

fun GenericRecord.getBooleanOrNull(key: String): Boolean? = get(key)?.let { it as Boolean }
fun GenericRecord.getBoolean(key: String) = getBooleanOrNull(key)!!

fun GenericRecord.getBigDecimalOrNull(key: String): BigDecimal? = get(key)?.let { it as BigDecimal }
fun GenericRecord.getBigDecimal(key: String) = getBigDecimalOrNull(key)!!

fun GenericRecord.getLongOrNull(key: String): Long? = get(key)?.let { it as Long }
fun GenericRecord.getLong(key: String) = getLongOrNull(key)!!

fun GenericRecord.getStringListOrNull(key: String): List<String>? = get(key)?.let { it as List<Any> }?.map { it.asString() }
fun GenericRecord.getStringList(key: String) = getStringListOrNull(key)!!

fun GenericRecord.getDoubleOrNull(key: String): Double? = get(key)?.let { it as Double }
fun GenericRecord.getDouble(key: String) = getDoubleOrNull(key)!!

fun GenericRecord.getInstantOrNull(key: String): Instant? = getStringOrNull(key)?.let(Instant::parse)
fun GenericRecord.getInstant(key: String) = getInstantOrNull(key)!!

fun GenericRecord.getEnumOrNull(key: String): String? = get(key)?.let { (get(key) as GenericData.EnumSymbol).toString() }
fun GenericRecord.getEnum(key: String) = getEnumOrNull(key)!!

fun GenericRecord.getRecordOrNull(key: String): GenericRecord? = get(key)?.let { get(key) as GenericRecord }
fun GenericRecord.getRecord(key: String) = getRecordOrNull(key)!!

fun GenericRecord.getRecordListOrNull(key: String): List<GenericRecord>? = get(key)?.let { it as List<GenericRecord> }
fun GenericRecord.getRecordList(key: String) = getRecordListOrNull(key)!!

fun GenericRecord.getHexStringAsByteArrayOrNull(key: String): ByteArray? = getStringOrNull(key)?.let(HexFormat.of()::parseHex)
fun GenericRecord.getHexStringAsByteArray(key: String): ByteArray = getHexStringAsByteArrayOrNull(key)!!

fun <T> GenericRecord.getRecordFromUnion(deserialize: (type: String, record: GenericRecord) -> T): T {

    val envelopeType = getString(EnvelopeFields.ENVELOPE_TYPE)
    val envelope = getRecord(EnvelopeFields.ENVELOPE)

    return deserialize(envelopeType, envelope)
}

fun <T : Any> GenericRecord.deserializeWith(deserializer: RecordDeserializer<T>): T = deserializer.deserialize(this)

fun <T : Any> GenericRecord.getAsValueOrNull(key: String, deserializer: RecordDeserializer<T>): T? = getRecordOrNull(key)?.deserializeWith(deserializer)
fun <T : Any> GenericRecord.getAsValue(key: String, deserializer: RecordDeserializer<T>): T = getAsValueOrNull(key, deserializer)!!

fun <T : Any> GenericRecord.getAsValuesOrNull(key: String, deserializer: RecordDeserializer<T>): List<T>? = getRecordListOrNull(key)?.map(deserializer::deserialize)
fun <T : Any> GenericRecord.getAsValues(key: String, deserializer: RecordDeserializer<T>): List<T> = getRecordListOrNull(key)?.map(deserializer::deserialize)!!