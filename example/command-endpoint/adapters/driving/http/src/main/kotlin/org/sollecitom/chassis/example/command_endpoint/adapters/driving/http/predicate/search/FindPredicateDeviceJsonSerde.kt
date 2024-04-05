package org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.predicate.search

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.example.event.domain.predicate.search.Description
import org.sollecitom.chassis.example.event.domain.predicate.search.Device
import org.sollecitom.chassis.example.event.domain.predicate.search.FindPredicateDevice
import org.sollecitom.chassis.example.event.domain.predicate.search.ProductCode
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.getStringOrNull
import org.sollecitom.chassis.json.utils.serde.JsonSerde

val FindPredicateDevice.Companion.serde: JsonSerde<FindPredicateDevice> get() = FindPredicateDeviceJsonSerde

private object FindPredicateDeviceJsonSerde : JsonSerde<FindPredicateDevice> {

    override fun serialize(value: FindPredicateDevice): JSONObject = JSONObject().put(Fields.EMAIL, JSONObject().put(Fields.ADDRESS, value.emailAddress.value)).put(Fields.DEVICE, JSONObject().put(Fields.DESCRIPTION, value.device.description.value).put(Fields.PRODUCT_CODE, value.device.productCode?.value))

    override fun deserialize(json: JSONObject): FindPredicateDevice {

        val emailAddress = json.getRequiredJSONObject(Fields.EMAIL).getRequiredString(Fields.ADDRESS).let(::EmailAddress)
        val deviceJson = json.getRequiredJSONObject(Fields.DEVICE)
        val description = deviceJson.getRequiredString(Fields.DESCRIPTION).let(::Name).let(::Description)
        val productCode = deviceJson.getStringOrNull(Fields.PRODUCT_CODE)?.let(::ProductCode)
        val device = Device(description, productCode)
        return FindPredicateDevice(emailAddress = emailAddress, device = device)
    }

    private object Fields {
        const val EMAIL = "email"
        const val ADDRESS = "address"
        const val DEVICE = "device"
        const val DESCRIPTION = "description"
        const val PRODUCT_CODE = "product-code"
    }
}