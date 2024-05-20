package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.session

import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.session.FederatedSession
import org.sollecitom.chassis.correlation.core.domain.access.session.Session
import org.sollecitom.chassis.correlation.core.domain.access.session.SimpleSession
import com.element.dpg.libs.chassis.json.utils.serde.getRequiredString
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde

private object SessionJsonSerde : JsonSerde.SchemaAware<Session> {

    private const val SCHEMA_LOCATION = "correlation/access/session/Session.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Session) = when (value) {
        is SimpleSession -> SimpleSession.jsonSerde.serialize(value)
        is FederatedSession -> FederatedSession.jsonSerde.serialize(value)
    }

    override fun deserialize(json: JSONObject) = when (val type = json.getRequiredString(Fields.TYPE)) {
        SimpleSessionJsonSerde.TYPE_VALUE -> SimpleSession.jsonSerde.deserialize(json)
        FederatedSessionJsonSerde.TYPE_VALUE -> FederatedSession.jsonSerde.deserialize(json)
        else -> error("Unsupported session type $type")
    }

    private object Fields {
        const val TYPE = "type"
    }
}

val Session.Companion.jsonSerde: JsonSerde.SchemaAware<Session> get() = SessionJsonSerde