package org.sollecitom.chassis.example.event.serialization.json.predicate.search

import org.json.JSONObject
import org.sollecitom.chassis.example.event.domain.predicate.search.DeviceInformation
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.*

internal object DeviceInformationJsonSerde : JsonSerde.SchemaAware<DeviceInformation> {

    private const val SCHEMA_LOCATION = "example/event/domain/predicate/search/DeviceInformation.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: DeviceInformation) = JSONObject().apply {
        setValue(Fields.DESCRIPTION, value.description, DeviceDescriptionJsonSerde)
        setValueOrNull(Fields.PRODUCT_CODE, value.productCode, ProductCodeJsonSerde)
    }

    override fun deserialize(json: JSONObject): DeviceInformation {

        val description = json.getValue(Fields.DESCRIPTION, DeviceDescriptionJsonSerde)
        val productCode = json.getValueOrNull(Fields.PRODUCT_CODE, ProductCodeJsonSerde)
        return DeviceInformation(description, productCode)
    }

    private object Fields {
        const val DESCRIPTION = "description"
        const val PRODUCT_CODE = "product-code"
    }
}
