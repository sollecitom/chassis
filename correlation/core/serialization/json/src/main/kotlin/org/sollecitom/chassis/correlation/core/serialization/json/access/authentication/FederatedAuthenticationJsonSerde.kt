package org.sollecitom.chassis.correlation.core.serialization.json.access.authentication

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.domain.access.authentication.FederatedAuthentication
import org.sollecitom.chassis.correlation.core.domain.access.session.FederatedSession
import org.sollecitom.chassis.correlation.core.serialization.json.access.session.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

internal object FederatedAuthenticationJsonSerde : JsonSerde.SchemaAware<FederatedAuthentication> {

    const val TYPE_VALUE = "federated"

    override val schema: Schema by lazy { jsonSchemaAt("correlation/access/authentication/FederatedAuthentication.json") }

    override fun serialize(value: FederatedAuthentication) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        put(Fields.TOKEN, Authentication.Token.jsonSerde.serialize(value.token))
        put(Fields.SESSION, FederatedSession.jsonSerde.serialize(value.session))
    }

    override fun deserialize(json: JSONObject): FederatedAuthentication {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val token = json.getRequiredJSONObject(Fields.TOKEN).let(Authentication.Token.jsonSerde::deserialize)
        val session = json.getRequiredJSONObject(Fields.SESSION).let(FederatedSession.jsonSerde::deserialize)
        return FederatedAuthentication(token = token, session = session)
    }

    private object Fields {
        const val TYPE = "type"
        const val TOKEN = "token"
        const val SESSION = "session"
    }
}

val FederatedAuthentication.Companion.jsonSerde: JsonSerde.SchemaAware<FederatedAuthentication> get() = FederatedAuthenticationJsonSerde