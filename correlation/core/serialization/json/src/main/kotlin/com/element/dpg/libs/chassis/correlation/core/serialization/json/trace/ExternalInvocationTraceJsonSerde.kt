package com.element.dpg.libs.chassis.correlation.core.serialization.json.trace

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde
import com.element.dpg.libs.chassis.correlation.core.domain.trace.ExternalInvocationTrace
import com.element.dpg.libs.chassis.json.utils.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.setValue
import org.json.JSONObject

private object ExternalInvocationTraceJsonSerde : JsonSerde.SchemaAware<ExternalInvocationTrace> {

    private const val SCHEMA_LOCATION = "correlation/trace/ExternalInvocationTrace.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: ExternalInvocationTrace) = JSONObject().apply {
        setValue(Fields.INVOCATION_ID, value.invocationId, Id.jsonSerde)
        setValue(Fields.ACTION_ID, value.actionId, Id.jsonSerde)
    }

    override fun deserialize(json: JSONObject): ExternalInvocationTrace {

        val invocationId = json.getValue(Fields.INVOCATION_ID, Id.jsonSerde)
        val actionId = json.getValue(Fields.ACTION_ID, Id.jsonSerde)
        return ExternalInvocationTrace(invocationId = invocationId, actionId = actionId)
    }

    private object Fields {
        const val INVOCATION_ID = "invocation-id"
        const val ACTION_ID = "action-id"
    }
}

val ExternalInvocationTrace.Companion.jsonSerde: JsonSerde.SchemaAware<ExternalInvocationTrace> get() = ExternalInvocationTraceJsonSerde