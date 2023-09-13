package org.sollecitom.chassis.correlation.core.serialization.json.access.session

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.access.session.SimpleSession
import org.sollecitom.chassis.correlation.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

internal object SimpleSessionJsonSerde : JsonSerde.SchemaAware<SimpleSession> {

    const val TYPE_VALUE = "simple"
    private const val SCHEMA_LOCATION = "correlation/access/session/SimpleSession.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: SimpleSession) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ID, value.id, Id.jsonSerde)
    }

    override fun deserialize(json: JSONObject): SimpleSession {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val id = json.getValue(Fields.ID, Id.jsonSerde)
        return SimpleSession(id = id)
    }

    private object Fields {
        const val ID = "id"
        const val TYPE = "type"
    }
}

val SimpleSession.Companion.jsonSerde: JsonSerde.SchemaAware<SimpleSession> get() = SimpleSessionJsonSerde