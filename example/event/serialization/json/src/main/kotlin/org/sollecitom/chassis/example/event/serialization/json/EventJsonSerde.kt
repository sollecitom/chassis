package org.sollecitom.chassis.example.event.serialization.json

import org.json.JSONObject
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.Happening
import org.sollecitom.chassis.ddd.serialization.json.happening.jsonSerde
import org.sollecitom.chassis.example.event.domain.UserEvent
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue

private object EventJsonSerde : JsonSerde.SchemaAware<Event> {

    private const val SCHEMA_LOCATION = "example/event/domain/Event.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Event) = when (value) {
        is UserEvent -> UserEventJsonSerde.serialize(value)
        else -> error("Unsupported event type ${value.type}")
    }

    override fun deserialize(json: JSONObject): Event {

        val type = json.getValue(Fields.TYPE, Happening.Type.jsonSerde)
        val serde: JsonSerde<out Event> = when {
            type in UserEventJsonSerde.types -> UserEventJsonSerde
            else -> error("Unsupported event type $type")
        }
        return serde.deserialize(json)
    }

    private object Fields {
        const val TYPE = "type"
    }
}

val Event.Companion.jsonSerde: JsonSerde.SchemaAware<Event> get() = EventJsonSerde