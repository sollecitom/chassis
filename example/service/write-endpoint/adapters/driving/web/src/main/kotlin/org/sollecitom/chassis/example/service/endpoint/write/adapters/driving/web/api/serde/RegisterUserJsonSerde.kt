package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.serde

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.serde.JsonSerde

val RegisterUser.V1.Companion.serde: JsonSerde<RegisterUser.V1> get() = RegisterUserCommandSerde.V1

private object RegisterUserCommandSerde {

    object V1 : JsonSerde<RegisterUser.V1> {

        override fun serialize(value: RegisterUser.V1): JSONObject = JSONObject().put(Fields.EMAIL, JSONObject().put(Fields.ADDRESS, value.emailAddress.value))

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