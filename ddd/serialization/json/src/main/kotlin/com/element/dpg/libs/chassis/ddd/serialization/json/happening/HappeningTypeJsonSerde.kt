package com.element.dpg.libs.chassis.ddd.serialization.json.happening

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.ddd.domain.Happening
import com.element.dpg.libs.chassis.json.utils.serde.getRequiredInt
import com.element.dpg.libs.chassis.json.utils.serde.getRequiredString
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde

internal object HappeningTypeJsonSerde : JsonSerde.SchemaAware<Happening.Type> {

    private const val SCHEMA_LOCATION = "ddd/happening/HappeningType.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Happening.Type) = JSONObject().apply {
        put(Fields.NAME, value.name.value)
        put(Fields.VERSION, value.version.value)
    }

    override fun deserialize(json: JSONObject): Happening.Type {

        val name = json.getRequiredString(Fields.NAME).let(::Name)
        val version = json.getRequiredInt(Fields.VERSION).let(::IntVersion)
        return Happening.Type(name, version)
    }

    private object Fields {
        const val NAME = "name"
        const val VERSION = "version"
    }
}

val Happening.Type.Companion.jsonSerde: JsonSerde.SchemaAware<Happening.Type> get() = HappeningTypeJsonSerde