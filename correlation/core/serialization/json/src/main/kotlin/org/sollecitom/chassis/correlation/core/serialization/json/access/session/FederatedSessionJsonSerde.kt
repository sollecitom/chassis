package org.sollecitom.chassis.correlation.core.serialization.json.access.session

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.fromString
import org.sollecitom.chassis.correlation.core.domain.access.idp.IdentityProvider
import org.sollecitom.chassis.correlation.core.domain.access.session.FederatedSession
import org.sollecitom.chassis.correlation.core.serialization.json.access.idp.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

internal object FederatedSessionJsonSerde : JsonSerde.SchemaAware<FederatedSession> {

    const val TYPE_VALUE = "federated"

    override val schema: Schema by lazy { jsonSchemaAt("access/session/FederatedSession.json") }

    override fun serialize(value: FederatedSession) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        put(Fields.ID, value.id.stringValue)
        put(Fields.IDENTITY_PROVIDER, IdentityProvider.jsonSerde.serialize(value.identityProvider))
    }

    override fun deserialize(json: JSONObject): FederatedSession {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '${TYPE_VALUE}'" }
        val id = json.getRequiredString(Fields.ID).let(Id.Companion::fromString)
        val identityProvider = json.getRequiredJSONObject(Fields.IDENTITY_PROVIDER).let(IdentityProvider.jsonSerde::deserialize)
        return FederatedSession(id = id, identityProvider = identityProvider)
    }

    private object Fields {
        const val TYPE = "type"
        const val ID = "id"
        const val IDENTITY_PROVIDER = "identity-provider"
    }
}

val FederatedSession.Companion.jsonSerde: JsonSerde.SchemaAware<FederatedSession> get() = FederatedSessionJsonSerde