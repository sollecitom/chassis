package org.sollecitom.chassis.correlation.core.serialization.json.access.authentication

import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.domain.access.authentication.StatelessAuthentication
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

internal object StatelessAuthenticationJsonSerde : JsonSerde.SchemaAware<StatelessAuthentication> {

    const val TYPE_VALUE = "stateless"
    private const val SCHEMA_LOCATION = "correlation/access/authentication/StatelessAuthentication.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: StatelessAuthentication) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.TOKEN, value.token, Authentication.Token.jsonSerde)
    }

    override fun deserialize(json: JSONObject): StatelessAuthentication {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val token = json.getValue(Fields.TOKEN, Authentication.Token.jsonSerde)
        return StatelessAuthentication(token = token)
    }

    private object Fields {
        const val TYPE = "type"
        const val TOKEN = "token"
    }
}

val StatelessAuthentication.Companion.jsonSerde: JsonSerde.SchemaAware<StatelessAuthentication> get() = StatelessAuthenticationJsonSerde