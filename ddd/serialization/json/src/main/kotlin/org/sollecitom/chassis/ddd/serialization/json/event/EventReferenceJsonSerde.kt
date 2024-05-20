package org.sollecitom.chassis.ddd.serialization.json.event

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.serialization.json.happening.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredInstant
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.putInstant
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

internal object EventReferenceJsonSerde : JsonSerde.SchemaAware<Event.Reference> {

    private const val SCHEMA_LOCATION = "ddd/event/EventReference.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Event.Reference) = JSONObject().apply {
        setValue(Fields.ID, value.id, Id.jsonSerde)
        putInstant(Fields.TIMESTAMP, value.timestamp)
        setValue(Fields.TYPE, value.type, com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde)
    }

    override fun deserialize(json: JSONObject): Event.Reference {

        val id = json.getValue(Fields.ID, Id.jsonSerde)
        val timestamp = json.getRequiredInstant(Fields.TIMESTAMP)
        val type = json.getValue(Fields.TYPE, com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde)
        return Event.Reference(id, type, timestamp)
    }

    private object Fields {
        const val ID = "id"
        const val TYPE = "type"
        const val TIMESTAMP = "timestamp"
    }
}

val Event.Reference.Companion.jsonSerde: JsonSerde.SchemaAware<Event.Reference> get() = EventReferenceJsonSerde