package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.authentication

import com.element.dpg.libs.chassis.correlation.core.domain.access.authentication.Authentication
import com.element.dpg.libs.chassis.correlation.core.domain.access.authentication.StatelessAuthentication
import com.element.dpg.libs.chassis.json.utils.getRequiredString
import com.element.dpg.libs.chassis.json.utils.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.setValue
import org.json.JSONObject

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