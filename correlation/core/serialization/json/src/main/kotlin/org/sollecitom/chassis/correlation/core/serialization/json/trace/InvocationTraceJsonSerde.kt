package org.sollecitom.chassis.correlation.core.serialization.json.trace

import kotlinx.datetime.Instant
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace
import org.sollecitom.chassis.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

private object InvocationTraceJsonSerde : JsonSerde.SchemaAware<InvocationTrace> {

    private const val SCHEMA_LOCATION = "correlation/trace/InvocationTrace.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: InvocationTrace) = JSONObject().apply {
        setValue(Fields.ID, value.id, Id.jsonSerde)
        put(Fields.CREATED_AT, value.createdAt.toString())
    }

    override fun deserialize(json: JSONObject): InvocationTrace {

        val id = json.getValue(Fields.ID, Id.jsonSerde)
        val createdAt = json.getRequiredString(Fields.CREATED_AT).let(Instant::parse)
        return InvocationTrace(id = id, createdAt = createdAt)
    }

    private object Fields {
        const val ID = "id"
        const val CREATED_AT = "created-at"
    }
}

val InvocationTrace.Companion.jsonSerde: JsonSerde.SchemaAware<InvocationTrace> get() = InvocationTraceJsonSerde