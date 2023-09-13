package org.sollecitom.chassis.correlation.core.serialization.json.trace

import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.trace.ExternalInvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.Trace
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

private object TraceJsonSerde : JsonSerde.SchemaAware<Trace> {

    private const val SCHEMA_LOCATION = "correlation/trace/Trace.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Trace) = JSONObject().apply {
        setValue(Fields.INVOCATION, value.invocation, InvocationTrace.jsonSerde)
        setValue(Fields.PARENT, value.parent, InvocationTrace.jsonSerde)
        setValue(Fields.ORIGINATING, value.originating, InvocationTrace.jsonSerde)
        setValue(Fields.EXTERNAL, value.external, ExternalInvocationTrace.jsonSerde)
    }

    override fun deserialize(json: JSONObject): Trace {

        val invocation = json.getValue(Fields.INVOCATION, InvocationTrace.jsonSerde)
        val parent = json.getValue(Fields.PARENT, InvocationTrace.jsonSerde)
        val originating = json.getValue(Fields.ORIGINATING, InvocationTrace.jsonSerde)
        val external = json.getValue(Fields.EXTERNAL, ExternalInvocationTrace.jsonSerde)
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