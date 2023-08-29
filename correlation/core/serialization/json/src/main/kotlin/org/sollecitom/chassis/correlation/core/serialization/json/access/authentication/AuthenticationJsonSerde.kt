package org.sollecitom.chassis.correlation.core.serialization.json.access.authentication

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.authentication.*
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

private object AuthenticationJsonSerde : JsonSerde.SchemaAware<Authentication> {

    override val schema: Schema by lazy { jsonSchemaAt("access/authentication/Authentication.json") }

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