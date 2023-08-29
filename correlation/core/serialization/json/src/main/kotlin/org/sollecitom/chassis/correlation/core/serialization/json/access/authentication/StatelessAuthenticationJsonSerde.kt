package org.sollecitom.chassis.correlation.core.serialization.json.access.authentication

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.domain.access.authentication.StatelessAuthentication
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

internal object StatelessAuthenticationJsonSerde : JsonSerde.SchemaAware<StatelessAuthentication> {

    const val TYPE_VALUE = "stateless"

    override val schema: Schema by lazy { jsonSchemaAt("access/authentication/StatelessAuthentication.json") }

    override fun serialize(value: StatelessAuthentication) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        put(Fields.TOKEN, Authentication.Token.jsonSerde.serialize(value.token))
    }

    override fun deserialize(json: JSONObject): StatelessAuthentication {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val token = json.getRequiredJSONObject(Fields.TOKEN).let(Authentication.Token.jsonSerde::deserialize)
        return StatelessAuthentication(token = token)
    }

    private object Fields {
        const val TYPE = "type"
        const val TOKEN = "token"
    }
}

val StatelessAuthentication.Companion.jsonSerde: JsonSerde.SchemaAware<StatelessAuthentication> get() = StatelessAuthenticationJsonSerde