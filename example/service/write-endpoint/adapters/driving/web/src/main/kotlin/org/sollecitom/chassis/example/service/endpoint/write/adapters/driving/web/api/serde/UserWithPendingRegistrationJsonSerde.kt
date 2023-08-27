package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.serde

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.ulid.ULID
import org.sollecitom.chassis.example.service.endpoint.write.application.user.UserWithPendingRegistration
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.serde.JsonSerde

val UserWithPendingRegistration.Companion.serde: JsonSerde<UserWithPendingRegistration> get() = UserWithPendingRegistrationJsonSerde

private object UserWithPendingRegistrationJsonSerde : JsonSerde<UserWithPendingRegistration> {

    override fun serialize(value: UserWithPendingRegistration): JSONObject = JSONObject().put(Fields.ID, value.id.stringValue)

    override fun deserialize(json: JSONObject): UserWithPendingRegistration {

        val id = json.getRequiredString(Fields.ID).let(ULID::invoke)
        return UserWithPendingRegistration(id)
    }

    private object Fields {
        const val ID = "id"
    }
}