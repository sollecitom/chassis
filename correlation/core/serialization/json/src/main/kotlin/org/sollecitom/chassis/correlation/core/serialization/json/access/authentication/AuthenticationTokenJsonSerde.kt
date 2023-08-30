package org.sollecitom.chassis.correlation.core.serialization.json.access.authentication

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.fromString
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.serialization.json.access.actor.ServiceAccountJsonSerde
import org.sollecitom.chassis.correlation.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.json.utils.getInstantOrNull
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

private object AuthenticationTokenJsonSerde : JsonSerde.SchemaAware<Authentication.Token> {

    override val schema: Schema by lazy { jsonSchemaAt("correlation/access/authentication/AuthenticationToken.json") }

    override fun serialize(value: Authentication.Token) = JSONObject().apply {
        put(Fields.ID, Id.jsonSerde.serialize(value.id))
        put(Fields.VALID_FROM, value.validFrom?.toString())
        put(Fields.VALID_TO, value.validTo?.toString())
    }

    override fun deserialize(json: JSONObject): Authentication.Token {

        val id = json.getRequiredJSONObject(Fields.ID).let(Id.jsonSerde::deserialize)
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