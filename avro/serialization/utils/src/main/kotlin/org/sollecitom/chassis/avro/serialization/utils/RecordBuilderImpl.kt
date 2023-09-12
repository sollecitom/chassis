package org.sollecitom.chassis.avro.serialization.utils

import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord
import org.apache.avro.generic.GenericRecordBuilder
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.util.*

internal class RecordBuilderImpl(schema: Schema) : GenericRecordBuilder(schema), RecordBuilder {

    constructor(schema: Schema, other: GenericRecord) : this(schema) {

        deepCopyFieldsWithTheirSchemaFromRecord(other)
    }

    override val schema: Schema get() = super.schema()

    override fun set(fieldName: String, value: String?): RecordBuilder = value.ifNotNullOrUnset(fieldName, ::setValue)
    override fun set(fieldName: String, value: Double?): RecordBuilder = value.ifNotNullOrUnset(fieldName, ::setValue)
    override fun set(fieldName: String, value: Int?): RecordBuilder = value.ifNotNullOrUnset(fieldName, ::setValue)
    override fun set(fieldName: String, value: BigInteger?): RecordBuilder = value.ifNotNullOrUnset(fieldName, ::setValue)
    override fun set(fieldName: String, value: BigDecimal?): RecordBuilder = value.ifNotNullOrUnset(fieldName, ::setValue)
    override fun set(fieldName: String, value: Long?): RecordBuilder = value.ifNotNullOrUnset(fieldName, ::setValue)
    override fun set(fieldName: String, value: Boolean?): RecordBuilder = value.ifNotNullOrUnset(fieldName, ::setValue)
    override fun set(fieldName: String, value: GenericRecord?): RecordBuilder = value.ifNotNullOrUnset(fieldName, ::setValue)
    override fun set(fieldName: String, value: Instant?): RecordBuilder = value?.toString().ifNotNullOrUnset(fieldName, ::setValue)
    override fun set(fieldName: String, value: Map<String, String>?): RecordBuilder = value.ifNotNullOrUnset(fieldName, ::setValue)
    override fun set(fieldName: String, bytes: ByteArray?): RecordBuilder = bytes?.let(HexFormat.of()::formatHex).ifNotNullOrUnset(fieldName, ::setValue)

    override fun setStrings(fieldName: String, value: List<String>?): RecordBuilder = value.ifNotNullOrUnset(fieldName, ::setValue)
    override fun setDoubles(fieldName: String, value: List<Double>?): RecordBuilder = value.ifNotNullOrUnset(fieldName, ::setValue)
    override fun setInts(fieldName: String, value: List<Int>?): RecordBuilder = value.ifNotNullOrUnset(fieldName, ::setValue)
    override fun setLongs(fieldName: String, value: List<Long>?): RecordBuilder = value.ifNotNullOrUnset(fieldName, ::setValue)
    override fun setBooleans(fieldName: String, value: List<Boolean>?): RecordBuilder = value.ifNotNullOrUnset(fieldName, ::setValue)

    override fun unset(fieldName: String): RecordBuilder {
        super.clear(fieldName)
        return this
    }

    override fun setEnum(fieldName: String, value: Any?): RecordBuilder = if (value != null) {
        val fieldSchema = schema.getEnumFieldSchema(fieldName)
        val genericEnumSymbol = GenericData.EnumSymbol(fieldSchema, value)
        super.set(fieldName, genericEnumSymbol)
        this
    } else {
        unset(fieldName)
    }

    override fun setRecordInUnion(unionType: String, record: GenericRecord?): RecordBuilder {

        super.set(EnvelopeFields.ENVELOPE_TYPE, unionType)
        super.set(EnvelopeFields.ENVELOPE, record)
        return this
    }

    override fun setRecordInUnion(unionType: String, customizeRecord: RecordBuilder.() -> Unit): RecordBuilder {

        setRecordInUnion(unionType, buildGenericRecord(actualUnionTypeSchema(unionType, schema), customizeRecord))
        return this
    }

    override fun setInstants(fieldName: String, value: List<Instant>?): RecordBuilder = value?.map(Instant::toString).ifNotNullOrUnset(fieldName, ::setStrings)

    override fun setRecords(fieldName: String, value: List<GenericRecord>?): RecordBuilder = if (value != null) {
        val fieldSchema = schema.fields.single { it.name() == fieldName }.schema()
        set(fieldName, GenericData.Array(fieldSchema, value))
        this
    } else {
        unset(fieldName)
    }

    private fun actualUnionTypeSchema(unionType: String, schema: Schema): Schema = schema.referencedSchemas().first { it.name == unionType }

    private fun Schema.getEnumFieldSchema(fieldName: String): Schema {

        val fieldSchema = getField(fieldName).schema()

        return when (fieldSchema.type) {
            Schema.Type.ENUM -> return fieldSchema
            Schema.Type.UNION -> return fieldSchema.types.first { it.type == Schema.Type.ENUM }
            else -> fieldSchema
        }
    }

    private fun setValue(fieldName: String, value: Any): RecordBuilder {

        super.set(fieldName, value)
        return this
    }

    private fun deepCopyFieldsWithTheirSchemaFromRecord(other: GenericRecord) {

        other.schema.fields.forEach { field ->
            val value = other[field.pos()]
            if (isValidValue(field, value)) {
                set(field, data().deepCopy(field.schema(), value))
            }
        }
    }

    private fun <T : Any> T?.ifNotNullOrUnset(fieldName: String, action: (String, T) -> RecordBuilder): RecordBuilder {

        return if (this != null) action(fieldName, this) else unset(fieldName)
    }
}

private fun Schema.referencedSchemas(): Set<Schema> {

    val toExplore = Stack<Schema>()
    toExplore.push(this)
    val descendants = mutableSetOf<Schema>()
    while (!toExplore.isEmpty()) {
        val currentSchema = toExplore.pop()
        val referencedSchemas = if (currentSchema.isUnion) {
            currentSchema.types
        } else {
            currentSchema.fields.map(Schema.Field::schema)
        }
        toExplore.addAll(referencedSchemas.filter(Schema::isUnionOrRecord).filter { it !in descendants })
        descendants += referencedSchemas
    }
    return descendants
}


private fun Schema.isUnionOrRecord(): Boolean = type == Schema.Type.UNION || type == Schema.Type.RECORD