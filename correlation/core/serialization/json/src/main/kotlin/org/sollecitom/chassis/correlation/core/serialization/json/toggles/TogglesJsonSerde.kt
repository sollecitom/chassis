package org.sollecitom.chassis.correlation.core.serialization.json.toggles

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.toggles.ToggleValue
import org.sollecitom.chassis.correlation.core.domain.toggles.Toggles
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValues
import org.sollecitom.chassis.json.utils.serde.setValues

private object TogglesJsonSerde : JsonSerde.SchemaAware<Toggles> {

    private const val SCHEMA_LOCATION = "correlation/toggles/Toggles.json"
    override val schema: Schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

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