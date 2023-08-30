package org.sollecitom.chassis.correlation.core.serialization.json.context

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.trace.Trace
import org.sollecitom.chassis.correlation.core.serialization.json.access.jsonSerde
import org.sollecitom.chassis.correlation.core.serialization.json.trace.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

private object InvocationContextJsonSerde : JsonSerde.SchemaAware<InvocationContext<*>> {

    override val schema: Schema by lazy { jsonSchemaAt("correlation/context/InvocationContext.json") }

    override fun serialize(value: InvocationContext<*>) = JSONObject().apply {
        put(Fields.ACCESS, Access.jsonSerde.serialize(value.access))
        put(Fields.TRACE, Trace.jsonSerde.serialize(value.trace))
    }

    override fun deserialize(json: JSONObject): InvocationContext<*> {

        val access = json.getRequiredJSONObject(Fields.ACCESS).let(Access.jsonSerde::deserialize)
        val trace = json.getRequiredJSONObject(Fields.TRACE).let(Trace.jsonSerde::deserialize)
        return InvocationContext(access = access, trace = trace)
    }

    private object Fields {
        const val ACCESS = "access"
        const val TRACE = "trace"
    }
}

val InvocationContext.Companion.jsonSerde: JsonSerde.SchemaAware<InvocationContext<*>> get() = InvocationContextJsonSerde