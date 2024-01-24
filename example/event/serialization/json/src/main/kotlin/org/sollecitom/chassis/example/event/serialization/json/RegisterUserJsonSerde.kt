package org.sollecitom.chassis.example.event.serialization.json

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.ddd.domain.Happening
import org.sollecitom.chassis.ddd.serialization.json.event.EventJsonSerdeSupport
import org.sollecitom.chassis.example.event.domain.UserRegistrationRequestWasAlreadySubmitted
import org.sollecitom.chassis.example.event.domain.user.registration.RegisterUser
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

internal object RegisterUserJsonSerde : JsonSerde.SchemaAware<RegisterUser> {

    private const val SCHEMA_LOCATION = "example/event/domain/user/registration/RegisterUserCommand.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: RegisterUser) = JSONObject().apply {
        put(Fields.EMAIL_ADDRESS, value.emailAddress.value)
    }

    override fun deserialize(json: JSONObject): RegisterUser {

        val emailAddress = json.getRequiredString(Fields.EMAIL_ADDRESS).let(::EmailAddress)
        return RegisterUser(emailAddress)
    }

    private object Fields {
        const val EMAIL_ADDRESS = "email-address"
    }
}
