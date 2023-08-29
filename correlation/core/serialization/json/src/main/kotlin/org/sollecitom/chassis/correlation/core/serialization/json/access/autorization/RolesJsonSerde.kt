package org.sollecitom.chassis.correlation.core.serialization.json.access.autorization

import com.github.erosb.jsonsKema.Schema
import org.json.JSONArray
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.authorization.Role
import org.sollecitom.chassis.correlation.core.domain.access.authorization.Roles
import org.sollecitom.chassis.json.utils.getRequiredJSONArray
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

private object RolesJsonSerde : JsonSerde.SchemaAware<Roles> {

    override val schema: Schema by lazy { jsonSchemaAt("access/authorization/Roles.json") }

    override fun serialize(value: Roles) = JSONObject().apply {

        put(Fields.VALUES, value.values.map(Role.jsonSerde::serialize).fold(JSONArray(), JSONArray::put))
    }

    override fun deserialize(json: JSONObject): Roles {

        val values = json.getRequiredJSONArray(Fields.VALUES).map { it as JSONObject }.map(Role.jsonSerde::deserialize).toSet()
        return Roles(values = values)
    }

    private object Fields {
        const val VALUES = "values"
    }
}

val Roles.Companion.jsonSerde: JsonSerde.SchemaAware<Roles> get() = RolesJsonSerde