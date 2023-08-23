package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.serde

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.ulid.ULID
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.User
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.serde.JsonSerde

val User.WithPendingRegistration.Companion.serde: JsonSerde<User.WithPendingRegistration> get() = UserWithPendingRegistrationJsonSerde

private object UserWithPendingRegistrationJsonSerde : JsonSerde<User.WithPendingRegistration> {

    override fun serialize(value: User.WithPendingRegistration): JSONObject = JSONObject().put(Fields.ID, value.id.stringValue)

    override fun deserialize(json: JSONObject): User.WithPendingRegistration {

        val id = json.getRequiredString(Fields.ID).let(ULID::invoke)
        return User.WithPendingRegistration(id)
    }

    private object Fields {
        const val ID = "id"
    }
}