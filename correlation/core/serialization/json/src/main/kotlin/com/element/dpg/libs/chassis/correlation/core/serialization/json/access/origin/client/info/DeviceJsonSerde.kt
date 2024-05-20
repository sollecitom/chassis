package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.origin.client.info

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.json.utils.serde.getStringOrNull
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde
import com.element.dpg.libs.chassis.web.client.info.domain.Device

private object DeviceJsonSerde : JsonSerde.SchemaAware<Device> {

    private const val SCHEMA_LOCATION = "correlation/access/origin/client/info/Device.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Device) = JSONObject().apply {
        value.className?.value?.let { put(Fields.CLASS_NAME, it) }
        value.name?.value?.let { put(Fields.NAME, it) }
        value.brand?.value?.let { put(Fields.BRAND, it) }
    }

    override fun deserialize(json: JSONObject): Device {

        val className = json.getStringOrNull(Fields.CLASS_NAME)?.let(::Name)
        val name = json.getStringOrNull(Fields.NAME)?.let(::Name)
        val brand = json.getStringOrNull(Fields.BRAND)?.let(::Name)
        return Device(className, name, brand)
    }

    private object Fields {
        const val CLASS_NAME = "class-name"
        const val NAME = "name"
        const val BRAND = "brand"
    }
}

val Device.Companion.jsonSerde: JsonSerde.SchemaAware<Device> get() = DeviceJsonSerde