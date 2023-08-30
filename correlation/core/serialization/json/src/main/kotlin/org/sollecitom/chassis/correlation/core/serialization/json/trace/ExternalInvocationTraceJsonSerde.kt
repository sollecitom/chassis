package org.sollecitom.chassis.correlation.core.serialization.json.trace

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.trace.ExternalInvocationTrace
import org.sollecitom.chassis.correlation.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

private object ExternalInvocationTraceJsonSerde : JsonSerde.SchemaAware<ExternalInvocationTrace> {

    override val schema: Schema by lazy { jsonSchemaAt("correlation/trace/ExternalInvocationTrace.json") }

    override fun serialize(value: ExternalInvocationTrace) = JSONObject().apply {
        put(Fields.INVOCATION_ID, Id.jsonSerde.serialize(value.invocationId))
        put(Fields.ACTION_ID, Id.jsonSerde.serialize(value.actionId))
    }

    override fun deserialize(json: JSONObject): ExternalInvocationTrace {

        val invocationId = json.getRequiredJSONObject(Fields.INVOCATION_ID).let(Id.jsonSerde::deserialize)
        val actionId = json.getRequiredJSONObject(Fields.ACTION_ID).let(Id.jsonSerde::deserialize)
        return ExternalInvocationTrace(invocationId = invocationId, actionId = actionId)
    }

    private object Fields {
        const val INVOCATION_ID = "invocation-id"
        const val ACTION_ID = "action-id"
    }
}

val ExternalInvocationTrace.Companion.jsonSerde: JsonSerde.SchemaAware<ExternalInvocationTrace> get() = ExternalInvocationTraceJsonSerde