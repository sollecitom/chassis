package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.authentication

import com.element.dpg.libs.chassis.correlation.core.domain.access.authentication.Authentication
import com.element.dpg.libs.chassis.correlation.core.domain.access.authentication.FederatedAuthentication
import com.element.dpg.libs.chassis.correlation.core.domain.access.session.FederatedSession
import com.element.dpg.libs.chassis.correlation.core.serialization.json.access.session.jsonSerde
import com.element.dpg.libs.chassis.json.utils.getRequiredString
import com.element.dpg.libs.chassis.json.utils.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.setValue
import org.json.JSONObject

internal object FederatedAuthenticationJsonSerde : JsonSerde.SchemaAware<FederatedAuthentication> {

    const val TYPE_VALUE = "federated"
    private const val SCHEMA_LOCATION = "correlation/access/authentication/FederatedAuthentication.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: FederatedAuthentication) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.TOKEN, value.token, Authentication.Token.jsonSerde)
        setValue(Fields.SESSION, value.session, FederatedSession.jsonSerde)
    }

    override fun deserialize(json: JSONObject): FederatedAuthentication {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val token = json.getValue(Fields.TOKEN, Authentication.Token.jsonSerde)
        val session = json.getValue(Fields.SESSION, FederatedSession.jsonSerde)
        return FederatedAuthentication(token = token, session = session)
    }

    private object Fields {
        const val TYPE = "type"
        const val TOKEN = "token"
        const val SESSION = "session"
    }
}

val FederatedAuthentication.Companion.jsonSerde: JsonSerde.SchemaAware<FederatedAuthentication> get() = FederatedAuthenticationJsonSerde