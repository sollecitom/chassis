package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.session

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde
import com.element.dpg.libs.chassis.correlation.core.domain.access.idp.IdentityProvider
import com.element.dpg.libs.chassis.correlation.core.domain.access.session.FederatedSession
import com.element.dpg.libs.chassis.correlation.core.serialization.json.access.idp.jsonSerde
import com.element.dpg.libs.chassis.json.utils.getRequiredString
import com.element.dpg.libs.chassis.json.utils.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.setValue
import org.json.JSONObject

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