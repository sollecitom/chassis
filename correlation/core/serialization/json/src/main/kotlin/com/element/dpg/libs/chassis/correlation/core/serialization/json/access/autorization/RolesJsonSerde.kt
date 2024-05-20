package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.autorization

import com.element.dpg.libs.chassis.correlation.core.domain.access.authorization.Role
import com.element.dpg.libs.chassis.correlation.core.domain.access.authorization.Roles
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.serde.getValues
import com.element.dpg.libs.chassis.json.utils.serde.serde.setValues
import org.json.JSONObject

private object RolesJsonSerde : JsonSerde.SchemaAware<Roles> {

    private const val SCHEMA_LOCATION = "correlation/access/authorization/Roles.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Roles) = JSONObject().apply {
        setValues(Fields.VALUES, value.values, Role.jsonSerde)
    }

    override fun deserialize(json: JSONObject): Roles {

        val values = json.getValues(Fields.VALUES, Role.jsonSerde)
        return Roles(values = values.toSet())
    }

    private object Fields {
        const val VALUES = "values"
    }
}

val Roles.Companion.jsonSerde: JsonSerde.SchemaAware<Roles> get() = RolesJsonSerde