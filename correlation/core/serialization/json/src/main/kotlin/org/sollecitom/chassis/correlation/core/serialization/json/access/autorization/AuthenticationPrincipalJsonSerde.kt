package org.sollecitom.chassis.correlation.core.serialization.json.access.autorization

import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import org.sollecitom.chassis.correlation.core.domain.access.authorization.Roles
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

private object AuthenticationPrincipalJsonSerde : JsonSerde.SchemaAware<AuthorizationPrincipal> {

    private const val SCHEMA_LOCATION = "correlation/access/authorization/AuthorizationPrincipal.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: AuthorizationPrincipal) = JSONObject().apply {
        setValue(Fields.ROLES, value.roles, Roles.jsonSerde)
    }

    override fun deserialize(json: JSONObject): AuthorizationPrincipal {

        val roles = json.getValue(Fields.ROLES, Roles.jsonSerde)
        return AuthorizationPrincipal(roles = roles)
    }

    private object Fields {
        const val ROLES = "roles"
    }
}

val AuthorizationPrincipal.Companion.jsonSerde: JsonSerde.SchemaAware<AuthorizationPrincipal> get() = AuthenticationPrincipalJsonSerde