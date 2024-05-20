package com.element.dpg.libs.chassis.correlation.core.serialization.json.access

import com.element.dpg.libs.chassis.correlation.core.domain.access.Access
import com.element.dpg.libs.chassis.json.utils.getRequiredString
import com.element.dpg.libs.chassis.json.utils.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import org.json.JSONObject

private object AccessJsonSerde : JsonSerde.SchemaAware<Access> {

    private const val SCHEMA_LOCATION = "correlation/access/Access.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Access) = when (value) {
        is Access.Authenticated -> Access.Authenticated.jsonSerde.serialize(value)
        is Access.Unauthenticated -> Access.Unauthenticated.jsonSerde.serialize(value)
    }

    override fun deserialize(json: JSONObject) = when (val type = json.getRequiredString(Fields.TYPE)) {
        AuthenticatedAccessJsonSerde.TYPE_VALUE -> Access.Authenticated.jsonSerde.deserialize(json)
        UnauthenticatedAccessJsonSerde.TYPE_VALUE -> Access.Unauthenticated.jsonSerde.deserialize(json)
        else -> error("Unsupported access type $type")
    }

    private object Fields {
        const val TYPE = "type"
    }
}

val Access.Companion.jsonSerde: JsonSerde.SchemaAware<Access> get() = AccessJsonSerde