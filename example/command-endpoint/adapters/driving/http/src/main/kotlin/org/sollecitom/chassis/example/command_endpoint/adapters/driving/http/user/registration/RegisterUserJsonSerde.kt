package org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.user.registration

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.example.event.domain.user.registration.RegisterUser
import org.sollecitom.chassis.example.event.domain.user.registration.UserWithPendingRegistration
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

val RegisterUser.Companion.serde: JsonSerde<RegisterUser> get() = RegisterUserCommandSerde
val RegisterUser.Result.Accepted.Companion.serde: JsonSerde<RegisterUser.Result.Accepted> get() = RegisterUserCommandSerde.Result.Accepted

private object RegisterUserCommandSerde : JsonSerde<RegisterUser> {


    override fun serialize(value: RegisterUser): JSONObject = JSONObject().put(Fields.EMAIL, JSONObject().put(Fields.ADDRESS, value.emailAddress.value))

    override fun deserialize(json: JSONObject): RegisterUser {

        val emailAddress = json.getRequiredJSONObject(Fields.EMAIL).getRequiredString(Fields.ADDRESS).let(::EmailAddress)
        return RegisterUser(emailAddress = emailAddress)
    }

    private object Fields {
        const val EMAIL = "email"
        const val ADDRESS = "address"
    }

    object Result {

        object Accepted : JsonSerde<RegisterUser.Result.Accepted> {

            override fun serialize(value: RegisterUser.Result.Accepted) = JSONObject().apply {

                setValue(Fields.USER, value.user, UserWithPendingRegistration.serde)
            }

            override fun deserialize(json: JSONObject): RegisterUser.Result.Accepted {

                val user = json.getValue(Fields.USER, UserWithPendingRegistration.serde)
                return RegisterUser.Result.Accepted(user)
            }

            private object Fields {
                const val USER = "user"
            }
        }
    }
}