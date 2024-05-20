package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.autorization

import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.correlation.core.domain.access.authorization.Role
import com.element.dpg.libs.chassis.json.utils.getRequiredString
import com.element.dpg.libs.chassis.json.utils.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import org.json.JSONObject

private object RoleJsonSerde : JsonSerde.SchemaAware<Role> {

    private const val SCHEMA_LOCATION = "correlation/access/authorization/Role.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Role) = JSONObject().apply {

        put(Fields.NAME, value.name.value)
    }

    override fun deserialize(json: JSONObject): Role {

        val name = json.getRequiredString(Fields.NAME).let(::Name)
        return Role(name = name)
    }

    private object Fields {
        const val NAME = "name"
    }
}

val Role.Companion.jsonSerde: JsonSerde.SchemaAware<Role> get() = RoleJsonSerde