package org.sollecitom.chassis.correlation.core.serialization.json.access.origin

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.networking.IpAddress
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin
import org.sollecitom.chassis.correlation.core.serialization.json.access.origin.client.info.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue
import org.sollecitom.chassis.web.client.info.domain.ClientInfo

private object OriginJsonSerde : JsonSerde.SchemaAware<Origin> {

    private const val SCHEMA_LOCATION = "correlation/access/origin/Origin.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Origin) = JSONObject().apply {
        put(Fields.IP_ADDRESS, value.ipAddress.stringValue)
        setValue(Fields.CLIENT_INFO, value.clientInfo, ClientInfo.jsonSerde)
    }

    override fun deserialize(json: JSONObject): Origin {

        val rawIpAddress = json.getRequiredString(Fields.IP_ADDRESS)
        val ipAddress = IpAddress.create(rawIpAddress)
        val clientInfo = json.getValue(Fields.CLIENT_INFO, ClientInfo.jsonSerde)
        return Origin(ipAddress = ipAddress, clientInfo = clientInfo)
    }

    private object Fields {
        const val IP_ADDRESS = "ip-address"
        const val CLIENT_INFO = "client-info"
    }
}

val Origin.Companion.jsonSerde: JsonSerde.SchemaAware<Origin> get() = OriginJsonSerde