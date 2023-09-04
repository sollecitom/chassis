package org.sollecitom.chassis.correlation.core.serialization.json.toggles

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.toggles.*
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

// TODO write a test for this
private object ToggleValueJsonSerde : JsonSerde.SchemaAware<ToggleValue<*>> {

    private const val SCHEMA_LOCATION = "correlation/toggles/ToggleValue.json"
    override val schema: Schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: ToggleValue<*>) = JSONObject().apply {
        when (value) {
            is BooleanToggleValue -> setValue(Fields.VALUE, value, BooleanToggleValue.jsonSerde)
            is DecimalToggleValue -> setValue(Fields.VALUE, value, DecimalToggleValue.jsonSerde)
            is IntegerToggleValue -> setValue(Fields.VALUE, value, IntegerToggleValue.jsonSerde)
            is EnumToggleValue -> setValue(Fields.VALUE, value, EnumToggleValue.jsonSerde)
        }
    }

    override fun deserialize(json: JSONObject) = when (val type = json.getRequiredString(Fields.TYPE)) {
        BooleanToggleValueJsonSerde.TYPE_VALUE -> json.getValue(Fields.VALUE, BooleanToggleValue.jsonSerde)
        DecimalToggleValueJsonSerde.TYPE_VALUE -> json.getValue(Fields.VALUE, DecimalToggleValue.jsonSerde)
        IntegerToggleValueJsonSerde.TYPE_VALUE -> json.getValue(Fields.VALUE, IntegerToggleValue.jsonSerde)
        EnumToggleValueJsonSerde.TYPE_VALUE -> json.getValue(Fields.VALUE, EnumToggleValue.jsonSerde)
        else -> error("Unsupported toggle value type $type")
    }
}

private object Fields {
    const val TYPE = "TYPE"
    const val VALUE = "value"
}

val ToggleValue.Companion.jsonSerde: JsonSerde.SchemaAware<ToggleValue<*>> get() = ToggleValueJsonSerde