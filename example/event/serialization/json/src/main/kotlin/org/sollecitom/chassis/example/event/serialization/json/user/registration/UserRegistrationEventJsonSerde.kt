package org.sollecitom.chassis.example.event.serialization.json.user.registration

import org.json.JSONObject
import org.sollecitom.chassis.ddd.domain.Happening
import org.sollecitom.chassis.ddd.serialization.json.event.EventJsonSerdeSupport
import org.sollecitom.chassis.ddd.serialization.json.happening.jsonSerde
import org.sollecitom.chassis.example.event.domain.user.registration.UserRegistrationEvent
import org.sollecitom.chassis.example.event.domain.user.registration.UserRegistrationRequestWasAlreadySubmitted
import org.sollecitom.chassis.example.event.domain.user.registration.UserRegistrationRequestWasSubmitted
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue

internal object UserRegistrationEventJsonSerde : JsonSerde.SchemaAware<UserRegistrationEvent>, EventJsonSerdeSupport.EntitySpecific<UserRegistrationEvent> {

    private const val SCHEMA_LOCATION = "example/event/domain/user/registration/UserRegistrationEvent.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    val types: Set<Happening.Type> = UserRegistrationRequestWasAlreadySubmittedEventJsonSerde.types + UserRegistrationRequestWasSubmittedEventJsonSerde.types

    override fun serialize(value: UserRegistrationEvent) = when (value) {
        is UserRegistrationRequestWasAlreadySubmitted -> UserRegistrationRequestWasAlreadySubmittedEventJsonSerde.serialize(value)
        is UserRegistrationRequestWasSubmitted -> UserRegistrationRequestWasSubmittedEventJsonSerde.serialize(value)
    }

    override fun deserialize(json: JSONObject): UserRegistrationEvent {

        val serde: JsonSerde<out UserRegistrationEvent> = when (val type = json.getValue(Fields.TYPE, Happening.Type.jsonSerde)) {
            in UserRegistrationRequestWasAlreadySubmittedEventJsonSerde.types -> UserRegistrationRequestWasAlreadySubmittedEventJsonSerde
            in UserRegistrationRequestWasSubmittedEventJsonSerde.types -> UserRegistrationRequestWasSubmittedEventJsonSerde
            else -> error("Unsupported event type $type")
        }
        return serde.deserialize(json)
    }

    private object Fields {
        const val TYPE = "type"
    }
}