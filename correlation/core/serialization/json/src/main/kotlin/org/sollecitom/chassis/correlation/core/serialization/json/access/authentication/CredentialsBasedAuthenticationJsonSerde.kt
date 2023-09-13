package org.sollecitom.chassis.correlation.core.serialization.json.access.authentication

import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.domain.access.authentication.CredentialsBasedAuthentication
import org.sollecitom.chassis.correlation.core.domain.access.session.SimpleSession
import org.sollecitom.chassis.correlation.core.serialization.json.access.session.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

internal object CredentialsBasedAuthenticationJsonSerde : JsonSerde.SchemaAware<CredentialsBasedAuthentication> {

    const val TYPE_VALUE = "credentials-based"
    private const val SCHEMA_LOCATION = "correlation/access/authentication/CredentialsBasedAuthentication.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: CredentialsBasedAuthentication) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.TOKEN, value.token, Authentication.Token.jsonSerde)
        setValue(Fields.SESSION, value.session, SimpleSession.jsonSerde)
    }

    override fun deserialize(json: JSONObject): CredentialsBasedAuthentication {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val token = json.getValue(Fields.TOKEN, Authentication.Token.jsonSerde)
        val session = json.getValue(Fields.SESSION, SimpleSession.jsonSerde)
        return CredentialsBasedAuthentication(token = token, session = session)
    }

    private object Fields {
        const val TYPE = "type"
        const val TOKEN = "token"
        const val SESSION = "session"
    }
}

val CredentialsBasedAuthentication.Companion.jsonSerde: JsonSerde.SchemaAware<CredentialsBasedAuthentication> get() = CredentialsBasedAuthenticationJsonSerde