package org.sollecitom.chassis.example.event.serialization.json.user.registration

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.ddd.domain.Happening
import org.sollecitom.chassis.ddd.serialization.json.event.EventJsonSerdeSupport
import org.sollecitom.chassis.example.event.domain.user.registration.UserRegistrationRequestWasSubmitted
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

internal object UserRegistrationRequestWasSubmittedEventJsonSerde : JsonSerde.SchemaAware<UserRegistrationRequestWasSubmitted>, EventJsonSerdeSupport.EntitySpecific<UserRegistrationRequestWasSubmitted> {

    private const val SCHEMA_LOCATION = "example/event/domain/user/registration/UserRegistrationRequestWasSubmittedEvent.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    val types: Set<Happening.Type> = setOf(UserRegistrationRequestWasSubmitted.V1.type)

    override fun serialize(value: UserRegistrationRequestWasSubmitted) = JSONObject().apply {
        setEventFields(value)
        put(Fields.EMAIL_ADDRESS, value.emailAddress.value)
    }

    override fun deserialize(json: JSONObject): UserRegistrationRequestWasSubmitted {

        val (id, timestamp, type, context, entityId) = json.getEventFields()
        val emailAddress = json.getRequiredString(Fields.EMAIL_ADDRESS).let(::EmailAddress)
        return when {
            type == UserRegistrationRequestWasSubmitted.V1.type -> UserRegistrationRequestWasSubmitted.V1(emailAddress = emailAddress, userId = entityId, id = id, timestamp = timestamp, context = context)
            else -> error("Invalid event type $type for UserRegistrationRequestWasSubmittedEvent JSON serde")
        }
    }

    private object Fields {
        const val EMAIL_ADDRESS = "email-address"
    }
}
