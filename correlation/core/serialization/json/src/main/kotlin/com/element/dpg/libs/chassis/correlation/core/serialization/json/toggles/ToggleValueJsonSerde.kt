package com.element.dpg.libs.chassis.correlation.core.serialization.json.toggles

import com.element.dpg.libs.chassis.correlation.core.domain.toggles.*
import com.element.dpg.libs.chassis.json.utils.getRequiredString
import com.element.dpg.libs.chassis.json.utils.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import org.json.JSONObject

private object ToggleValueJsonSerde : JsonSerde.SchemaAware<ToggleValue<*>> {

    private const val SCHEMA_LOCATION = "correlation/toggles/ToggleValue.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: ToggleValue<*>) = when (value) {
        is BooleanToggleValue -> BooleanToggleValue.jsonSerde.serialize(value)
        is DecimalToggleValue -> DecimalToggleValue.jsonSerde.serialize(value)
        is IntegerToggleValue -> IntegerToggleValue.jsonSerde.serialize(value)
        is EnumToggleValue -> EnumToggleValue.jsonSerde.serialize(value)
    }

    override fun deserialize(json: JSONObject) = when (val type = json.getRequiredString(Fields.TYPE)) {
        BooleanToggleValueJsonSerde.TYPE_VALUE -> BooleanToggleValue.jsonSerde.deserialize(json)
        DecimalToggleValueJsonSerde.TYPE_VALUE -> DecimalToggleValue.jsonSerde.deserialize(json)
        IntegerToggleValueJsonSerde.TYPE_VALUE -> IntegerToggleValue.jsonSerde.deserialize(json)
        EnumToggleValueJsonSerde.TYPE_VALUE -> EnumToggleValue.jsonSerde.deserialize(json)
        else -> error("Unsupported toggle value type $type")
    }
}

private object Fields {
    const val TYPE = "type"
}

val ToggleValue.Companion.jsonSerde: JsonSerde.SchemaAware<ToggleValue<*>> get() = ToggleValueJsonSerde