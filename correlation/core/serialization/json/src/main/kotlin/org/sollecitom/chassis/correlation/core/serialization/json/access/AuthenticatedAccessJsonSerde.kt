package org.sollecitom.chassis.correlation.core.serialization.json.access

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin
import org.sollecitom.chassis.correlation.core.serialization.json.access.actor.jsonSerde
import org.sollecitom.chassis.correlation.core.serialization.json.access.autorization.jsonSerde
import org.sollecitom.chassis.correlation.core.serialization.json.access.origin.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

internal object AuthenticatedAccessJsonSerde : JsonSerde.SchemaAware<Access.Authenticated> {

    const val TYPE_VALUE = "authenticated"
    private const val SCHEMA_LOCATION = "correlation/access/AuthenticatedAccess.json"
    override val schema: Schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Access.Authenticated) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ACTOR, value.actor, Actor.jsonSerde)
        setValue(Fields.ORIGIN, value.origin, Origin.jsonSerde)
        setValue(Fields.AUTHORIZATION, value.authorization, AuthorizationPrincipal.jsonSerde)
    }

    override fun deserialize(json: JSONObject): Access.Authenticated {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val actor = json.getValue(Fields.ACTOR, Actor.jsonSerde)
        val origin = json.getValue(Fields.ORIGIN, Origin.jsonSerde)
        val authorization = json.getValue(Fields.AUTHORIZATION, AuthorizationPrincipal.jsonSerde)
        return Access.Authenticated(actor = actor, origin = origin, authorization = authorization)
    }

    private object Fields {
        const val TYPE = "type"
        const val ACTOR = "actor"
        const val ORIGIN = "origin"
        const val AUTHORIZATION = "authorization"
    }
}

val Access.Authenticated.Companion.jsonSerde: JsonSerde.SchemaAware<Access.Authenticated> get() = AuthenticatedAccessJsonSerde