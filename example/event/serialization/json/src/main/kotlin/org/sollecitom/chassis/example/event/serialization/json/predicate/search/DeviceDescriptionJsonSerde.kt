package org.sollecitom.chassis.example.event.serialization.json.predicate.search

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.example.event.domain.predicate.search.DeviceDescription
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

internal object DeviceDescriptionJsonSerde : JsonSerde.SchemaAware<DeviceDescription> {

    private const val SCHEMA_LOCATION = "example/event/domain/predicate/search/DeviceDescription.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: DeviceDescription) = JSONObject().apply {
        put(Fields.VALUE, value.value.value)
    }

    override fun deserialize(json: JSONObject): DeviceDescription {

        val value = json.getRequiredString(Fields.VALUE).let(::Name)
        return DeviceDescription(value)
    }

    private object Fields {
        const val VALUE = "value"
    }
}
