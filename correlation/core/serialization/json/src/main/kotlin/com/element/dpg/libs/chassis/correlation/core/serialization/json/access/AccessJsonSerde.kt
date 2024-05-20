package com.element.dpg.libs.chassis.correlation.core.serialization.json.access

import org.json.JSONObject
import com.element.dpg.libs.chassis.json.utils.serde.getRequiredString
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde

private object AccessJsonSerde : JsonSerde.SchemaAware<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access> {

    private const val SCHEMA_LOCATION = "correlation/access/Access.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access) = when (value) {
        is _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated -> _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated.jsonSerde.serialize(value)
        is _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated -> _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated.jsonSerde.serialize(value)
    }

    override fun deserialize(json: JSONObject) = when (val type = json.getRequiredString(com.element.dpg.libs.chassis.correlation.core.serialization.json.access.AccessJsonSerde.Fields.TYPE)) {
        com.element.dpg.libs.chassis.correlation.core.serialization.json.access.AuthenticatedAccessJsonSerde.TYPE_VALUE -> _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated.jsonSerde.deserialize(json)
        com.element.dpg.libs.chassis.correlation.core.serialization.json.access.UnauthenticatedAccessJsonSerde.TYPE_VALUE -> _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated.jsonSerde.deserialize(json)
        else -> error("Unsupported access type $type")
    }

    private object Fields {
        const val TYPE = "type"
    }
}

val _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Companion.jsonSerde: JsonSerde.SchemaAware<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access> get() = com.element.dpg.libs.chassis.correlation.core.serialization.json.access.AccessJsonSerde