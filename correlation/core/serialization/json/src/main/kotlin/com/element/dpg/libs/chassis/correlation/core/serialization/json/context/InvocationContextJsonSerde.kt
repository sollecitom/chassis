package com.element.dpg.libs.chassis.correlation.core.serialization.json.context

import com.element.dpg.libs.chassis.correlation.core.domain.access.Access
import com.element.dpg.libs.chassis.correlation.core.domain.context.InvocationContext
import com.element.dpg.libs.chassis.correlation.core.domain.tenancy.Tenant
import com.element.dpg.libs.chassis.correlation.core.domain.toggles.Toggles
import com.element.dpg.libs.chassis.correlation.core.domain.trace.Trace
import com.element.dpg.libs.chassis.correlation.core.serialization.json.access.jsonSerde
import com.element.dpg.libs.chassis.correlation.core.serialization.json.tenancy.jsonSerde
import com.element.dpg.libs.chassis.correlation.core.serialization.json.toggles.jsonSerde
import com.element.dpg.libs.chassis.correlation.core.serialization.json.trace.jsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.*
import org.json.JSONObject

private object InvocationContextJsonSerde : JsonSerde.SchemaAware<InvocationContext<*>> {

    private const val SCHEMA_LOCATION = "correlation/context/InvocationContext.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: InvocationContext<*>) = JSONObject().apply {
        setValue(Fields.ACCESS, value.access, Access.jsonSerde)
        setValue(Fields.TRACE, value.trace, Trace.jsonSerde)
        setValue(Fields.TOGGLES, value.toggles, Toggles.jsonSerde)
        setValueOrNull(Fields.SPECIFIED_TARGET_TENANT, value.specifiedTargetTenant, Tenant.jsonSerde)
    }

    override fun deserialize(json: JSONObject): InvocationContext<*> {

        val access = json.getValue(Fields.ACCESS, Access.jsonSerde)
        val trace = json.getValue(Fields.TRACE, Trace.jsonSerde)
        val toggles = json.getValue(Fields.TOGGLES, Toggles.jsonSerde)
        val tenant = json.getValueOrNull(Fields.SPECIFIED_TARGET_TENANT, Tenant.jsonSerde)
        return InvocationContext(access = access, trace = trace, toggles = toggles, specifiedTargetTenant = tenant)
    }

    private object Fields {
        const val ACCESS = "access"
        const val TRACE = "trace"
        const val TOGGLES = "toggles"
        const val SPECIFIED_TARGET_TENANT = "specified-target-tenant"
    }
}

val InvocationContext.Companion.jsonSerde: JsonSerde.SchemaAware<InvocationContext<*>> get() = InvocationContextJsonSerde