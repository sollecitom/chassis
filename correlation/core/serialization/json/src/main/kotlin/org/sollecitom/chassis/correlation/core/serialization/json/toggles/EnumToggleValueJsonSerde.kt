package org.sollecitom.chassis.correlation.core.serialization.json.toggles

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.correlation.core.domain.toggles.EnumToggleValue
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

internal object EnumToggleValueJsonSerde : JsonSerde.SchemaAware<EnumToggleValue> {

    const val TYPE_VALUE = "enum"
    private const val SCHEMA_LOCATION = "correlation/toggles/EnumToggleValue.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: EnumToggleValue) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ID, value.id, Id.jsonSerde)
        put(Fields.VALUE, value.value)
    }

    override fun deserialize(json: JSONObject): EnumToggleValue {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '${TYPE_VALUE}'" }
        val id = json.getValue(Fields.ID, Id.jsonSerde)
        val value = json.getRequiredString(Fields.VALUE)
        return EnumToggleValue(id = id, value = value)
    }

    private object Fields {
        const val ID = "id"
        const val TYPE = "type"
        const val VALUE = "value"
    }
}

val EnumToggleValue.Companion.jsonSerde: JsonSerde.SchemaAware<EnumToggleValue> get() = EnumToggleValueJsonSerde