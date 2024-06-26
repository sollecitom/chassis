package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.authentication

import com.element.dpg.libs.chassis.correlation.core.domain.access.authentication.Authentication
import com.element.dpg.libs.chassis.correlation.core.domain.access.authentication.CredentialsBasedAuthentication
import com.element.dpg.libs.chassis.correlation.core.domain.access.authentication.FederatedAuthentication
import com.element.dpg.libs.chassis.correlation.core.domain.access.authentication.StatelessAuthentication
import com.element.dpg.libs.chassis.json.utils.getRequiredString
import com.element.dpg.libs.chassis.json.utils.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import org.json.JSONObject

private object AuthenticationJsonSerde : JsonSerde.SchemaAware<Authentication> {

    private const val SCHEMA_LOCATION = "correlation/access/authentication/Authentication.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Authentication) = when (value) {
        is CredentialsBasedAuthentication -> CredentialsBasedAuthentication.jsonSerde.serialize(value)
        is FederatedAuthentication -> FederatedAuthentication.jsonSerde.serialize(value)
        is StatelessAuthentication -> StatelessAuthentication.jsonSerde.serialize(value)
    }

    override fun deserialize(json: JSONObject) = when (val type = json.getRequiredString(Fields.TYPE)) {
        CredentialsBasedAuthenticationJsonSerde.TYPE_VALUE -> CredentialsBasedAuthenticationJsonSerde.deserialize(json)
        FederatedAuthenticationJsonSerde.TYPE_VALUE -> FederatedAuthenticationJsonSerde.deserialize(json)
        StatelessAuthenticationJsonSerde.TYPE_VALUE -> StatelessAuthenticationJsonSerde.deserialize(json)
        else -> error("Unsupported authentication type $type")
    }

    private object Fields {
        const val TYPE = "type"
    }
}

val Authentication.Companion.jsonSerde: JsonSerde.SchemaAware<Authentication> get() = AuthenticationJsonSerde