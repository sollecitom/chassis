package com.element.dpg.libs.chassis.correlation.core.serialization.json.toggles

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.correlation.core.domain.toggles.BooleanToggleValue
import com.element.dpg.libs.chassis.json.utils.serde.getRequiredString
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.serde.setValue

internal object BooleanToggleValueJsonSerde : JsonSerde.SchemaAware<BooleanToggleValue> {

    const val TYPE_VALUE = "boolean"
    private const val SCHEMA_LOCATION = "correlation/toggles/BooleanToggleValue.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: BooleanToggleValue) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ID, value.id, Id.jsonSerde)
        put(Fields.VALUE, value.value)
    }

    override fun deserialize(json: JSONObject): BooleanToggleValue {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '${TYPE_VALUE}'" }
        val id = json.getValue(Fields.ID, Id.jsonSerde)
        val value = json.getBoolean(Fields.VALUE)
        return BooleanToggleValue(id = id, value = value)
    }

    private object Fields {
        const val ID = "id"
        const val TYPE = "type"
        const val VALUE = "value"
    }
}

val BooleanToggleValue.Companion.jsonSerde: JsonSerde.SchemaAware<BooleanToggleValue> get() = BooleanToggleValueJsonSerde