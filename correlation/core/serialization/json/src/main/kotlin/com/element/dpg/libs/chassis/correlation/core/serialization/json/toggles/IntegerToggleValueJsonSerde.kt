package com.element.dpg.libs.chassis.correlation.core.serialization.json.toggles

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde
import com.element.dpg.libs.chassis.correlation.core.domain.toggles.IntegerToggleValue
import com.element.dpg.libs.chassis.json.utils.serde.getRequiredString
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.serde.setValue
import org.json.JSONObject

internal object IntegerToggleValueJsonSerde : JsonSerde.SchemaAware<IntegerToggleValue> {

    const val TYPE_VALUE = "integer"
    private const val SCHEMA_LOCATION = "correlation/toggles/IntegerToggleValue.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: IntegerToggleValue) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ID, value.id, Id.jsonSerde)
        put(Fields.VALUE, value.value)
    }

    override fun deserialize(json: JSONObject): IntegerToggleValue {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '${TYPE_VALUE}'" }
        val id = json.getValue(Fields.ID, Id.jsonSerde)
        val value = json.getLong(Fields.VALUE)
        return IntegerToggleValue(id = id, value = value)
    }

    private object Fields {
        const val ID = "id"
        const val TYPE = "type"
        const val VALUE = "value"
    }
}

val IntegerToggleValue.Companion.jsonSerde: JsonSerde.SchemaAware<IntegerToggleValue> get() = IntegerToggleValueJsonSerde