package com.element.dpg.libs.chassis.correlation.core.serialization.json.toggles

import com.element.dpg.libs.chassis.correlation.core.domain.toggles.ToggleValue
import com.element.dpg.libs.chassis.correlation.core.domain.toggles.Toggles
import com.element.dpg.libs.chassis.json.utils.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getValues
import com.element.dpg.libs.chassis.json.utils.serde.setValues
import org.json.JSONObject

private object TogglesJsonSerde : JsonSerde.SchemaAware<Toggles> {

    private const val SCHEMA_LOCATION = "correlation/toggles/Toggles.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Toggles) = JSONObject().apply {
        setValues(Fields.VALUES, value.values, ToggleValue.jsonSerde)
    }

    override fun deserialize(json: JSONObject): Toggles {

        val values = json.getValues(Fields.VALUES, ToggleValue.jsonSerde)
        return Toggles(values = values.toSet())
    }

    private object Fields {
        const val VALUES = "values"
    }
}

val Toggles.Companion.jsonSerde: JsonSerde.SchemaAware<Toggles> get() = TogglesJsonSerde