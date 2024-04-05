package org.sollecitom.chassis.example.event.serialization.json.predicate.search

import org.json.JSONObject
import org.sollecitom.chassis.example.event.domain.predicate.search.ProductCode
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

internal object ProductCodeJsonSerde : JsonSerde.SchemaAware<ProductCode> {

    private const val SCHEMA_LOCATION = "example/event/domain/predicate/search/ProductCode.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: ProductCode) = JSONObject().apply {
        put(Fields.VALUE, value.value)
    }

    override fun deserialize(json: JSONObject): ProductCode {

        val value = json.getRequiredString(Fields.VALUE)
        return ProductCode(value)
    }

    private object Fields {
        const val VALUE = "value"
    }
}
