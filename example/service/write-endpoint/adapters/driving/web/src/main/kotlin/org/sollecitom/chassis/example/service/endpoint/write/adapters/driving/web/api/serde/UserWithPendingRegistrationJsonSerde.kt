package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.serde

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.example.service.endpoint.write.application.user.UserWithPendingRegistration
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

val UserWithPendingRegistration.Companion.serde: JsonSerde<UserWithPendingRegistration> get() = UserWithPendingRegistrationJsonSerde

private object UserWithPendingRegistrationJsonSerde : JsonSerde<UserWithPendingRegistration> {

    override fun serialize(value: UserWithPendingRegistration) = JSONObject().apply {
        setValue(Fields.ID, value.id, Id.jsonSerde)
    }

    override fun deserialize(json: JSONObject): UserWithPendingRegistration {

        val id = json.getValue(Fields.ID, Id.jsonSerde)
        return UserWithPendingRegistration(id)
    }

    private object Fields {
        const val ID = "id"
    }
}