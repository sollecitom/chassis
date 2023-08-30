package org.sollecitom.chassis.correlation.core.serialization.json.access.autorization

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.access.authorization.Roles
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

private object AuthenticationPrincipalJsonSerde : JsonSerde.SchemaAware<AuthorizationPrincipal> {

    override val schema: Schema by lazy { jsonSchemaAt("correlation/access/authorization/AuthorizationPrincipal.json") }

    override fun serialize(value: AuthorizationPrincipal) = JSONObject().apply {

        put(Fields.ROLES, Roles.jsonSerde.serialize(value.roles))
    }

    override fun deserialize(json: JSONObject): AuthorizationPrincipal {

        val roles = json.getRequiredJSONObject(Fields.ROLES).let(Roles.jsonSerde::deserialize)
        return AuthorizationPrincipal(roles = roles)
    }

    private object Fields {
        const val ROLES = "roles"
    }
}

val AuthorizationPrincipal.Companion.jsonSerde: JsonSerde.SchemaAware<AuthorizationPrincipal> get() = AuthenticationPrincipalJsonSerde