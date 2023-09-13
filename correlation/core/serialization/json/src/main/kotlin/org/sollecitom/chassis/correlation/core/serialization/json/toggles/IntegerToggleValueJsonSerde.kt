package org.sollecitom.chassis.correlation.core.serialization.json.toggles

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.toggles.IntegerToggleValue
import org.sollecitom.chassis.correlation.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

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