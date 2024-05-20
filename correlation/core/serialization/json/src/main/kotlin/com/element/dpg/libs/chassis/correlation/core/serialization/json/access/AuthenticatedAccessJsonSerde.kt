package com.element.dpg.libs.chassis.correlation.core.serialization.json.access

import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin
import org.sollecitom.chassis.correlation.core.domain.access.scope.AccessScope
import org.sollecitom.chassis.correlation.core.serialization.json.access.actor.jsonSerde
import org.sollecitom.chassis.correlation.core.serialization.json.access.autorization.jsonSerde
import org.sollecitom.chassis.correlation.core.serialization.json.access.origin.jsonSerde
import org.sollecitom.chassis.correlation.core.serialization.json.access.scope.jsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getRequiredString
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.serde.setValue

internal object AuthenticatedAccessJsonSerde : JsonSerde.SchemaAware<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated> {

    const val TYPE_VALUE = "authenticated"
    private const val SCHEMA_LOCATION = "correlation/access/AuthenticatedAccess.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ACTOR, value.actor, Actor.jsonSerde)
        setValue(Fields.ORIGIN, value.origin, Origin.jsonSerde)
        setValue(Fields.AUTHORIZATION, value.authorization, AuthorizationPrincipal.jsonSerde)
        setValue(Fields.SCOPE, value.scope, AccessScope.jsonSerde)
    }

    override fun deserialize(json: JSONObject): _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val actor = json.getValue(Fields.ACTOR, Actor.jsonSerde)
        val origin = json.getValue(Fields.ORIGIN, Origin.jsonSerde)
        val authorization = json.getValue(Fields.AUTHORIZATION, AuthorizationPrincipal.jsonSerde)
        val scope = json.getValue(Fields.SCOPE, AccessScope.jsonSerde)
        return _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated(actor = actor, origin = origin, authorization = authorization, scope = scope)
    }

    private object Fields {
        const val TYPE = "type"
        const val ACTOR = "actor"
        const val ORIGIN = "origin"
        const val AUTHORIZATION = "authorization"
        const val SCOPE = "scope"
    }
}

val _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated.Companion.jsonSerde: JsonSerde.SchemaAware<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated> get() = AuthenticatedAccessJsonSerde