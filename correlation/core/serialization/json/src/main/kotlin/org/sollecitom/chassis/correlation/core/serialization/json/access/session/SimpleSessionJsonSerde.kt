package org.sollecitom.chassis.correlation.core.serialization.json.access.session

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.fromString
import org.sollecitom.chassis.correlation.core.domain.access.session.SimpleSession
import org.sollecitom.chassis.correlation.core.serialization.json.access.actor.ServiceAccountJsonSerde
import org.sollecitom.chassis.correlation.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

internal object SimpleSessionJsonSerde : JsonSerde.SchemaAware<SimpleSession> {

    const val TYPE_VALUE = "simple"

    override val schema: Schema by lazy { jsonSchemaAt("access/session/SimpleSession.json") }

    override fun serialize(value: SimpleSession) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        put(Fields.ID, Id.jsonSerde.serialize(value.id))
    }

    override fun deserialize(json: JSONObject): SimpleSession {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val id = json.getRequiredJSONObject(Fields.ID).let(Id.jsonSerde::deserialize)
        return SimpleSession(id = id)
    }

    private object Fields {
        const val ID = "id"
        const val TYPE = "type"
    }
}

val SimpleSession.Companion.jsonSerde: JsonSerde.SchemaAware<SimpleSession> get() = SimpleSessionJsonSerde