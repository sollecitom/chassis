package org.sollecitom.chassis.correlation.core.serialization.json.access.session

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.access.idp.IdentityProvider
import org.sollecitom.chassis.correlation.core.domain.access.session.FederatedSession
import org.sollecitom.chassis.correlation.core.serialization.json.access.idp.jsonSerde
import org.sollecitom.chassis.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

internal object FederatedSessionJsonSerde : JsonSerde.SchemaAware<FederatedSession> {

    const val TYPE_VALUE = "federated"
    private const val SCHEMA_LOCATION = "correlation/access/session/FederatedSession.json\""
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: FederatedSession) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ID, value.id, Id.jsonSerde)
        setValue(Fields.IDENTITY_PROVIDER, value.identityProvider, IdentityProvider.jsonSerde)
    }

    override fun deserialize(json: JSONObject): FederatedSession {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '${TYPE_VALUE}'" }
        val id = json.getValue(Fields.ID, Id.jsonSerde)
        val identityProvider = json.getValue(Fields.IDENTITY_PROVIDER, IdentityProvider.jsonSerde)
        return FederatedSession(id = id, identityProvider = identityProvider)
    }

    private object Fields {
        const val TYPE = "type"
        const val ID = "id"
        const val IDENTITY_PROVIDER = "identity-provider"
    }
}

val FederatedSession.Companion.jsonSerde: JsonSerde.SchemaAware<FederatedSession> get() = FederatedSessionJsonSerde