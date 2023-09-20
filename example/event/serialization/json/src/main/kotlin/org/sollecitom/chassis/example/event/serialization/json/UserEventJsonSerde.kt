package org.sollecitom.chassis.example.event.serialization.json

import org.json.JSONObject
import org.sollecitom.chassis.ddd.domain.Happening
import org.sollecitom.chassis.ddd.serialization.json.event.EventJsonSerdeSupport
import org.sollecitom.chassis.ddd.serialization.json.happening.jsonSerde
import org.sollecitom.chassis.example.event.domain.UserEvent
import org.sollecitom.chassis.example.event.domain.UserRegistrationEvent
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue

internal object UserEventJsonSerde : JsonSerde.SchemaAware<UserEvent>, EventJsonSerdeSupport.EntitySpecific<UserEvent> {

    private const val SCHEMA_LOCATION = "example/event/domain/user/UserEvent.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    val types: Set<Happening.Type> = UserRegistrationEventJsonSerde.types

    override fun serialize(value: UserEvent) = when (value) {
        is UserRegistrationEvent -> UserRegistrationEventJsonSerde.serialize(value)
    }

    override fun deserialize(json: JSONObject): UserEvent {

        val serde: JsonSerde<out UserEvent> = when (val type = json.getValue(Fields.TYPE, Happening.Type.jsonSerde)) {
            in UserRegistrationEventJsonSerde.types -> UserRegistrationEventJsonSerde
            else -> error("Unsupported event type $type")
        }
        return serde.deserialize(json)
    }

    private object Fields {
        const val TYPE = "type"
    }
}