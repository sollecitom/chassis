package org.sollecitom.chassis.correlation.core.serialization.json.access.autorization

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.authorization.Role
import org.sollecitom.chassis.correlation.core.domain.access.authorization.Roles
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValues
import org.sollecitom.chassis.json.utils.serde.setValues

private object RolesJsonSerde : JsonSerde.SchemaAware<Roles> {

    private const val SCHEMA_LOCATION = "correlation/access/authorization/Roles.json"
    override val schema: Schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

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