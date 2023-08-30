package org.sollecitom.chassis.correlation.core.serialization.json.trace

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.trace.ExternalInvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.Trace
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

private object TraceJsonSerde : JsonSerde.SchemaAware<Trace> {

    override val schema: Schema by lazy { jsonSchemaAt("correlation/trace/Trace.json") }

    override fun serialize(value: Trace) = JSONObject().apply {
        put(Fields.INVOCATION, InvocationTrace.jsonSerde.serialize(value.invocation))
        put(Fields.PARENT, InvocationTrace.jsonSerde.serialize(value.parent))
        put(Fields.ORIGINATING, InvocationTrace.jsonSerde.serialize(value.originating))
        put(Fields.EXTERNAL, ExternalInvocationTrace.jsonSerde.serialize(value.external))
    }

    override fun deserialize(json: JSONObject): Trace {

        val invocation = json.getRequiredJSONObject(Fields.INVOCATION).let(InvocationTrace.jsonSerde::deserialize)
        val parent = json.getRequiredJSONObject(Fields.PARENT).let(InvocationTrace.jsonSerde::deserialize)
        val originating = json.getRequiredJSONObject(Fields.ORIGINATING).let(InvocationTrace.jsonSerde::deserialize)
        val external = json.getRequiredJSONObject(Fields.EXTERNAL).let(ExternalInvocationTrace.jsonSerde::deserialize)
        return Trace(invocation = invocation, parent = parent, originating = originating, external = external)
    }

    private object Fields {
        const val INVOCATION = "invocation"
        const val PARENT = "parent"
        const val ORIGINATING = "originating"
        const val EXTERNAL = "external"
    }
}

val Trace.Companion.jsonSerde: JsonSerde.SchemaAware<Trace> get() = TraceJsonSerde