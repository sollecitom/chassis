package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.origin.client.info

import com.element.dpg.libs.chassis.json.utils.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.setValue
import com.element.dpg.libs.chassis.web.client.info.domain.*
import org.json.JSONObject

private object ClientInfoJsonSerde : JsonSerde.SchemaAware<ClientInfo> {

    private const val SCHEMA_LOCATION = "correlation/access/origin/client/info/ClientInfo.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: ClientInfo) = JSONObject().apply {
        setValue(Fields.DEVICE, value.device, Device.jsonSerde)
        setValue(Fields.OPERATING_SYSTEM, value.operatingSystem, OperatingSystem.jsonSerde)
        setValue(Fields.LAYOUT_ENGINE, value.layoutEngine, LayoutEngine.jsonSerde)
        setValue(Fields.AGENT, value.agent, Agent.jsonSerde)
    }

    override fun deserialize(json: JSONObject): ClientInfo {

        val device = json.getValue(Fields.DEVICE, Device.jsonSerde)
        val operatingSystem = json.getValue(Fields.OPERATING_SYSTEM, OperatingSystem.jsonSerde)
        val layoutEngine = json.getValue(Fields.LAYOUT_ENGINE, LayoutEngine.jsonSerde)
        val agent = json.getValue(Fields.AGENT, Agent.jsonSerde)
        return ClientInfo(device, operatingSystem, layoutEngine, agent)
    }

    private object Fields {
        const val DEVICE = "device"
        const val OPERATING_SYSTEM = "operating-system"
        const val LAYOUT_ENGINE = "layout-engine"
        const val AGENT = "agent"
    }
}

val ClientInfo.Companion.jsonSerde: JsonSerde.SchemaAware<ClientInfo> get() = ClientInfoJsonSerde