package org.sollecitom.chassis.avro.serialization.utils

import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant

fun GenericRecord.copy(schema: Schema = getSchema(), customise: RecordBuilder.() -> Unit): GenericRecord = buildGenericRecord(this, schema, customise)

interface RecordBuilder {

    val schema: Schema

    fun build(): GenericRecord

    fun set(fieldName: String, value: String?): RecordBuilder
    fun set(fieldName: String, value: Double?): RecordBuilder
    fun set(fieldName: String, value: Int?): RecordBuilder
    fun set(fieldName: String, value: BigInteger?): RecordBuilder
    fun set(fieldName: String, value: BigDecimal?): RecordBuilder
    fun set(fieldName: String, value: Long?): RecordBuilder
    fun set(fieldName: String, value: Boolean?): RecordBuilder
    fun set(fieldName: String, value: GenericRecord?): RecordBuilder
    fun set(fieldName: String, value: Instant?): RecordBuilder
    fun set(fieldName: String, value: Map<String, String>?): RecordBuilder
    fun set(fieldName: String, bytes: ByteArray?): RecordBuilder
    fun unset(fieldName: String): RecordBuilder

    fun setStrings(fieldName: String, value: List<String>?): RecordBuilder
    fun setDoubles(fieldName: String, value: List<Double>?): RecordBuilder
    fun setInts(fieldName: String, value: List<Int>?): RecordBuilder
    fun setLongs(fieldName: String, value: List<Long>?): RecordBuilder
    fun setBooleans(fieldName: String, value: List<Boolean>?): RecordBuilder
    fun setEnum(fieldName: String, value: Any?): RecordBuilder
    fun setRecordInUnion(unionType: String, record: GenericRecord?): RecordBuilder
    fun setRecordInUnion(unionType: String, customizeRecord: RecordBuilder.() -> Unit): RecordBuilder
    fun setInstants(fieldName: String, value: List<Instant>?): RecordBuilder

    fun setRecords(fieldName: String, value: List<GenericRecord>?): RecordBuilder

    companion object
}

operator fun RecordBuilder.Companion.invoke(schema: Schema): RecordBuilder = RecordBuilderImpl(schema)
operator fun RecordBuilder.Companion.invoke(recordToCopy: GenericRecord, schema: Schema = recordToCopy.schema): RecordBuilder = RecordBuilderImpl(schema, recordToCopy)

inline fun buildGenericRecord(schema: Schema, customise: RecordBuilder.() -> Unit): GenericRecord = RecordBuilder(schema).also(customise).build()
inline fun buildGenericRecord(recordToCopy: GenericRecord, schema: Schema = recordToCopy.schema, customise: RecordBuilder.() -> Unit): GenericRecord = RecordBuilder(recordToCopy, schema).also(customise).build()



