package org.sollecitom.chassis.example.event.serialization.json.predicate.search

import org.json.JSONObject
import org.sollecitom.chassis.example.event.domain.predicate.search.Device
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.*

internal object DeviceJsonSerde : JsonSerde.SchemaAware<Device> {

    private const val SCHEMA_LOCATION = "example/event/domain/predicate/search/Device.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Device) = JSONObject().apply {
        setValue(Fields.DESCRIPTION, value.description, DescriptionJsonSerde)
        setValueOrNull(Fields.PRODUCT_CODE, value.productCode, ProductCodeJsonSerde)
    }

    override fun deserialize(json: JSONObject): Device {

        val description = json.getValue(Fields.DESCRIPTION, DescriptionJsonSerde)
        val productCode = json.getValueOrNull(Fields.PRODUCT_CODE, ProductCodeJsonSerde)
        return Device(description, productCode)
    }

    private object Fields {
        const val DESCRIPTION = "description"
        const val PRODUCT_CODE = "product-code"
    }
}
