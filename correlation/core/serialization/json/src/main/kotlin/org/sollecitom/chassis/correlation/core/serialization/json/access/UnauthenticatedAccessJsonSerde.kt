package org.sollecitom.chassis.correlation.core.serialization.json.access

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin
import org.sollecitom.chassis.correlation.core.serialization.json.access.autorization.jsonSerde
import org.sollecitom.chassis.correlation.core.serialization.json.access.origin.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

internal object UnauthenticatedAccessJsonSerde : JsonSerde.SchemaAware<Access.Unauthenticated> {

    const val TYPE_VALUE = "unauthenticated"
    private const val SCHEMA_LOCATION = "correlation/access/UnauthenticatedAccess.json"
    override val schema: Schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Access.Unauthenticated) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ORIGIN, value.origin, Origin.jsonSerde)
        setValue(Fields.AUTHORIZATION, value.authorization, AuthorizationPrincipal.jsonSerde)
    }

    override fun deserialize(json: JSONObject): Access.Unauthenticated {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val origin = json.getValue(Fields.ORIGIN, Origin.jsonSerde)
        val authorization = json.getValue(Fields.AUTHORIZATION, AuthorizationPrincipal.jsonSerde)
        return Access.Unauthenticated(origin = origin, authorization = authorization)
    }

    private object Fields {
        const val TYPE = "type"
        const val ORIGIN = "origin"
        const val AUTHORIZATION = "authorization"
    }
}

val Access.Unauthenticated.Companion.jsonSerde: JsonSerde.SchemaAware<Access.Unauthenticated> get() = UnauthenticatedAccessJsonSerde