package org.sollecitom.chassis.example.event.serialization.json.user.registration

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.ddd.domain.Happening
import org.sollecitom.chassis.ddd.serialization.json.event.EventJsonSerdeSupport
import org.sollecitom.chassis.example.event.domain.user.registration.UserRegistrationRequestWasAlreadySubmitted
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

internal object UserRegistrationRequestWasAlreadySubmittedEventJsonSerde : JsonSerde.SchemaAware<UserRegistrationRequestWasAlreadySubmitted>, EventJsonSerdeSupport.EntitySpecific<UserRegistrationRequestWasAlreadySubmitted> {

    private const val SCHEMA_LOCATION = "example/event/domain/user/registration/UserRegistrationRequestWasAlreadySubmittedEvent.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    val types: Set<Happening.Type> = setOf(UserRegistrationRequestWasAlreadySubmitted.V1.type)

    override fun serialize(value: UserRegistrationRequestWasAlreadySubmitted) = JSONObject().apply {
        setEventFields(value)
        put(Fields.EMAIL_ADDRESS, value.emailAddress.value)
    }

    override fun deserialize(json: JSONObject): UserRegistrationRequestWasAlreadySubmitted {

        val (id, timestamp, type, context, entityId) = json.getEventFields()
        val emailAddress = json.getRequiredString(Fields.EMAIL_ADDRESS).let(::EmailAddress)
        return when {
            type == UserRegistrationRequestWasAlreadySubmitted.V1.type -> UserRegistrationRequestWasAlreadySubmitted.V1(emailAddress = emailAddress, userId = entityId, id = id, timestamp = timestamp, context = context)
            else -> error("Invalid event type $type for UserRegistrationRequestWasAlreadySubmittedEvent JSON serde")
        }
    }

    private object Fields {
        const val EMAIL_ADDRESS = "email-address"
    }
}
