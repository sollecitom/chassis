package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.serde

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.serde.JsonDeserializer

val RegisterUser.V1.Companion.deserializer: JsonDeserializer<RegisterUser.V1> get() = RegisterUserCommandDeserializer.V1

private object RegisterUserCommandDeserializer {

    object V1 : JsonDeserializer<RegisterUser.V1> {

        override fun deserialize(json: JSONObject): RegisterUser.V1 {

            val emailAddress = json.getRequiredJSONObject(Fields.EMAIL).getRequiredString(Fields.ADDRESS).let(::EmailAddress)
            return RegisterUser.V1(emailAddress = emailAddress)
        }

        private object Fields {
            const val EMAIL = "email"
            const val ADDRESS = "address"
        }
    }
}