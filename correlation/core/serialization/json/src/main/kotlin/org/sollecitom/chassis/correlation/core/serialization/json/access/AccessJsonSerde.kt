package org.sollecitom.chassis.correlation.core.serialization.json.access

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

private object AccessJsonSerde : JsonSerde.SchemaAware<Access> {

    override val schema: Schema by lazy { jsonSchemaAt("access/Access.json") }

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