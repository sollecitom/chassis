package org.sollecitom.chassis.correlation.core.serialization.json.trace

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.correlation.core.domain.trace.ExternalInvocationTrace
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

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