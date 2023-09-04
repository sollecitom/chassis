package org.sollecitom.chassis.correlation.core.serialization.json.context

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.toggles.Toggles
import org.sollecitom.chassis.correlation.core.domain.trace.Trace
import org.sollecitom.chassis.correlation.core.serialization.json.access.jsonSerde
import org.sollecitom.chassis.correlation.core.serialization.json.toggles.jsonSerde
import org.sollecitom.chassis.correlation.core.serialization.json.trace.jsonSerde
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

private object InvocationContextJsonSerde : JsonSerde.SchemaAware<InvocationContext<*>> {

    private const val SCHEMA_LOCATION = "correlation/context/InvocationContext.json"
    override val schema: Schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: InvocationContext<*>) = JSONObject().apply {
        setValue(Fields.ACCESS, value.access, Access.jsonSerde)
        setValue(Fields.TRACE, value.trace, Trace.jsonSerde)
        setValue(Fields.TOGGLES, value.toggles, Toggles.jsonSerde)
    }

    override fun deserialize(json: JSONObject): InvocationContext<*> {

        val access = json.getValue(Fields.ACCESS, Access.jsonSerde)
        val trace = json.getValue(Fields.TRACE, Trace.jsonSerde)
        val toggles = json.getValue(Fields.TOGGLES, Toggles.jsonSerde)
        return InvocationContext(access = access, trace = trace, toggles = toggles)
    }

    private object Fields {
        const val ACCESS = "access"
        const val TRACE = "trace"
        const val TOGGLES = "toggles"
    }
}

val InvocationContext.Companion.jsonSerde: JsonSerde.SchemaAware<InvocationContext<*>> get() = InvocationContextJsonSerde