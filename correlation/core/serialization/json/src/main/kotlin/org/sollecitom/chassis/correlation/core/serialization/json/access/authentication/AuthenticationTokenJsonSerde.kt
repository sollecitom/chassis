package org.sollecitom.chassis.correlation.core.serialization.json.access.authentication

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.json.utils.getInstantOrNull
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

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