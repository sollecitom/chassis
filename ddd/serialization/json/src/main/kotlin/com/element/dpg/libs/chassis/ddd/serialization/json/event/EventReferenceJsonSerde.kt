package com.element.dpg.libs.chassis.ddd.serialization.json.event

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde
import com.element.dpg.libs.chassis.ddd.domain.Event
import com.element.dpg.libs.chassis.ddd.domain.Happening
import com.element.dpg.libs.chassis.ddd.serialization.json.happening.jsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getRequiredInstant
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.putInstant
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.serde.setValue
import org.json.JSONObject

internal object EventReferenceJsonSerde : JsonSerde.SchemaAware<Event.Reference> {

    private const val SCHEMA_LOCATION = "ddd/event/EventReference.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Event.Reference) = JSONObject().apply {
        setValue(Fields.ID, value.id, Id.jsonSerde)
        putInstant(Fields.TIMESTAMP, value.timestamp)
        setValue(Fields.TYPE, value.type, Happening.Type.jsonSerde)
    }

    override fun deserialize(json: JSONObject): Event.Reference {

        val id = json.getValue(Fields.ID, Id.jsonSerde)
        val timestamp = json.getRequiredInstant(Fields.TIMESTAMP)
        val type = json.getValue(Fields.TYPE, Happening.Type.jsonSerde)
        return Event.Reference(id, type, timestamp)
    }

    private object Fields {
        const val ID = "id"
        const val TYPE = "type"
        const val TIMESTAMP = "timestamp"
    }
}

val Event.Reference.Companion.jsonSerde: JsonSerde.SchemaAware<Event.Reference> get() = EventReferenceJsonSerde