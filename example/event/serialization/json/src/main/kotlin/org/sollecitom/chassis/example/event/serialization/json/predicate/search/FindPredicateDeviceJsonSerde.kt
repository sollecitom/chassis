package org.sollecitom.chassis.example.event.serialization.json.predicate.search

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.example.event.domain.predicate.search.FindPredicateDevice
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

internal object FindPredicateDeviceJsonSerde : JsonSerde.SchemaAware<FindPredicateDevice> {

    private const val SCHEMA_LOCATION = "example/event/domain/predicate/search/FindPredicateDeviceCommand.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: FindPredicateDevice) = JSONObject().apply {
        put(Fields.EMAIL_ADDRESS, value.emailAddress.value)
        setValue(Fields.DEVICE, value.device, DeviceJsonSerde)
    }

    override fun deserialize(json: JSONObject): FindPredicateDevice {

        val emailAddress = json.getRequiredString(Fields.EMAIL_ADDRESS).let(::EmailAddress)
        val device = json.getValue(Fields.DEVICE, DeviceJsonSerde)
        return FindPredicateDevice(emailAddress, device)
    }

    private object Fields {
        const val EMAIL_ADDRESS = "email-address"
        const val DEVICE = "device"
    }
}
