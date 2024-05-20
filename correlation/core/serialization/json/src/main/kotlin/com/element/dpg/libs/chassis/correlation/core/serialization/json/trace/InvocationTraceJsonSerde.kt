package com.element.dpg.libs.chassis.correlation.core.serialization.json.trace

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde
import com.element.dpg.libs.chassis.correlation.core.domain.trace.InvocationTrace
import com.element.dpg.libs.chassis.json.utils.serde.getRequiredString
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.serde.setValue
import kotlinx.datetime.Instant
import org.json.JSONObject

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