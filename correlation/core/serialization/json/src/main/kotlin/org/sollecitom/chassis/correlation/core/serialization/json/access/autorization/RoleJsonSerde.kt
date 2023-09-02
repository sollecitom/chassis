package org.sollecitom.chassis.correlation.core.serialization.json.access.autorization

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.correlation.core.domain.access.authorization.Role
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

private object RoleJsonSerde : JsonSerde.SchemaAware<Role> {

    private const val SCHEMA_LOCATION = "correlation/access/authorization/Role.json"
    override val schema: Schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

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