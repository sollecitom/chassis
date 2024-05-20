package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.origin.client.info

import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.json.utils.serde.getStringOrNull
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.serde.getValueOrNull
import com.element.dpg.libs.chassis.json.utils.serde.serde.setValue
import com.element.dpg.libs.chassis.web.client.info.domain.OperatingSystem
import com.element.dpg.libs.chassis.web.client.info.domain.Version
import org.json.JSONObject

private object OperatingSystemJsonSerde : JsonSerde.SchemaAware<OperatingSystem> {

    private const val SCHEMA_LOCATION = "correlation/access/origin/client/info/OperatingSystem.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: OperatingSystem) = JSONObject().apply {
        value.className?.value?.let { put(Fields.CLASS_NAME, it) }
        value.name?.value?.let { put(Fields.NAME, it) }
        value.version?.let { setValue(Fields.VERSION, it, Version.jsonSerde) }
    }

    override fun deserialize(json: JSONObject): OperatingSystem {

        val className = json.getStringOrNull(Fields.CLASS_NAME)?.let(::Name)
        val name = json.getStringOrNull(Fields.NAME)?.let(::Name)
        val version = json.getValueOrNull(Fields.VERSION, Version.jsonSerde)
        return OperatingSystem(className, name, version)
    }

    private object Fields {
        const val CLASS_NAME = "class-name"
        const val NAME = "name"
        const val VERSION = "version"
    }
}

val OperatingSystem.Companion.jsonSerde: JsonSerde.SchemaAware<OperatingSystem> get() = OperatingSystemJsonSerde