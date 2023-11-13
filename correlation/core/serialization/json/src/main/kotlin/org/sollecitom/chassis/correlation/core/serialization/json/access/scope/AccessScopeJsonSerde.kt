package org.sollecitom.chassis.correlation.core.serialization.json.access.scope

import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.scope.AccessContainer
import org.sollecitom.chassis.correlation.core.domain.access.scope.AccessScope
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValues
import org.sollecitom.chassis.json.utils.serde.setValues

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