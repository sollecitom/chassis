package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.authentication

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde
import com.element.dpg.libs.chassis.correlation.core.domain.access.authentication.Authentication
import com.element.dpg.libs.chassis.json.utils.getInstantOrNull
import com.element.dpg.libs.chassis.json.utils.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.setValue
import org.json.JSONObject

private object AuthenticationTokenJsonSerde : JsonSerde.SchemaAware<Authentication.Token> {

    private const val SCHEMA_LOCATION = "correlation/access/authentication/AuthenticationToken.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Authentication.Token) = JSONObject().apply {
        setValue(Fields.ID, value.id, Id.jsonSerde)
        put(Fields.VALID_FROM, value.validFrom?.toString())
        put(Fields.VALID_TO, value.validTo?.toString())
    }

    override fun deserialize(json: JSONObject): Authentication.Token {

        val id = json.getValue(Fields.ID, Id.jsonSerde)
        val validFrom = json.getInstantOrNull(Fields.VALID_FROM)
        val validTo = json.getInstantOrNull(Fields.VALID_TO)
        return Authentication.Token(id = id, validFrom = validFrom, validTo = validTo)
    }

    private object Fields {
        const val ID = "id"
        const val VALID_FROM = "valid-from"
        const val VALID_TO = "valid-to"
    }
}

val Authentication.Token.Companion.jsonSerde: JsonSerde.SchemaAware<Authentication.Token> get() = AuthenticationTokenJsonSerde