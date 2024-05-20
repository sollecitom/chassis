package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.autorization

import com.element.dpg.libs.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import com.element.dpg.libs.chassis.correlation.core.domain.access.authorization.Roles
import com.element.dpg.libs.chassis.json.utils.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.setValue
import org.json.JSONObject

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