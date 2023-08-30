package org.sollecitom.chassis.correlation.core.serialization.json.access

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin
import org.sollecitom.chassis.correlation.core.serialization.json.access.autorization.jsonSerde
import org.sollecitom.chassis.correlation.core.serialization.json.access.origin.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

internal object UnauthenticatedAccessJsonSerde : JsonSerde.SchemaAware<Access.Unauthenticated> {

    const val TYPE_VALUE = "unauthenticated"

    override val schema: Schema by lazy { jsonSchemaAt("correlation/access/UnauthenticatedAccess.json") }

    override fun serialize(value: Access.Unauthenticated) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        put(Fields.ORIGIN, Origin.jsonSerde.serialize(value.origin))
        put(Fields.AUTHORIZATION, AuthorizationPrincipal.jsonSerde.serialize(value.authorization))
    }

    override fun deserialize(json: JSONObject): Access.Unauthenticated {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val origin = json.getRequiredJSONObject(Fields.ORIGIN).let(Origin.jsonSerde::deserialize)
        val authorization = json.getRequiredJSONObject(Fields.AUTHORIZATION).let(AuthorizationPrincipal.jsonSerde::deserialize)
        return Access.Unauthenticated(origin = origin, authorization = authorization)
    }

    private object Fields {
        const val TYPE = "type"
        const val ORIGIN = "origin"
        const val AUTHORIZATION = "authorization"
    }
}

val Access.Unauthenticated.Companion.jsonSerde: JsonSerde.SchemaAware<Access.Unauthenticated> get() = UnauthenticatedAccessJsonSerde