package com.element.dpg.libs.chassis.correlation.core.serialization.json.toggles

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde
import com.element.dpg.libs.chassis.correlation.core.domain.toggles.DecimalToggleValue
import com.element.dpg.libs.chassis.json.utils.getRequiredString
import com.element.dpg.libs.chassis.json.utils.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.setValue
import org.json.JSONObject

internal object DecimalToggleValueJsonSerde : JsonSerde.SchemaAware<DecimalToggleValue> {

    const val TYPE_VALUE = "decimal"
    private const val SCHEMA_LOCATION = "correlation/toggles/DecimalToggleValue.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: DecimalToggleValue) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ID, value.id, Id.jsonSerde)
        put(Fields.VALUE, value.value)
    }

    override fun deserialize(json: JSONObject): DecimalToggleValue {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '${TYPE_VALUE}'" }
        val id = json.getValue(Fields.ID, Id.jsonSerde)
        val value = json.getDouble(Fields.VALUE)
        return DecimalToggleValue(id = id, value = value)
    }

    private object Fields {
        const val ID = "id"
        const val TYPE = "type"
        const val VALUE = "value"
    }
}

val DecimalToggleValue.Companion.jsonSerde: JsonSerde.SchemaAware<DecimalToggleValue> get() = DecimalToggleValueJsonSerde