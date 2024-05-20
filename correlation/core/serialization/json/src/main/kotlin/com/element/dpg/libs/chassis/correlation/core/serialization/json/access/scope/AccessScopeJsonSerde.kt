package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.scope

import com.element.dpg.libs.chassis.correlation.core.domain.access.scope.AccessContainer
import com.element.dpg.libs.chassis.correlation.core.domain.access.scope.AccessScope
import com.element.dpg.libs.chassis.json.utils.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getValues
import com.element.dpg.libs.chassis.json.utils.serde.setValues
import org.json.JSONObject

private object AccessScopeJsonSerde : JsonSerde.SchemaAware<AccessScope> {

    private const val SCHEMA_LOCATION = "correlation/access/scope/AccessScope.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: AccessScope) = JSONObject().apply {
        setValues(Fields.CONTAINER_STACK, value.containers, AccessContainer.jsonSerde)
    }

    override fun deserialize(json: JSONObject): AccessScope {

        val containerStack = json.getValues(Fields.CONTAINER_STACK, AccessContainer.jsonSerde)
        return AccessScope(containerStack = containerStack)
    }

    private object Fields {
        const val CONTAINER_STACK = "container-stack"
    }
}

val AccessScope.Companion.jsonSerde: JsonSerde.SchemaAware<AccessScope> get() = AccessScopeJsonSerde