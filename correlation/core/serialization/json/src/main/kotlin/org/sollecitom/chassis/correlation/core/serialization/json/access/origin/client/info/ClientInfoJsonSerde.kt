package org.sollecitom.chassis.correlation.core.serialization.json.access.origin.client.info

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.networking.IpAddress
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue
import org.sollecitom.chassis.web.client.info.domain.*

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