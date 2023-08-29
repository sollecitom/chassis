package org.sollecitom.chassis.correlation.core.serialization.json.access.origin

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.networking.IpAddress
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

private object OriginJsonSerde : JsonSerde.SchemaAware<Origin> {

    override val schema: Schema by lazy { jsonSchemaAt("access/origin/Origin.json") }

    override fun serialize(value: Origin) = JSONObject().apply {
        put(Fields.IP_ADDRESS, value.ipAddress.stringValue)
    }

    override fun deserialize(json: JSONObject): Origin {

        val rawIpAddress = json.getRequiredString(Fields.IP_ADDRESS)
        val ipAddress = IpAddress.create(rawIpAddress)
        return Origin(ipAddress = ipAddress)
    }

    private object Fields {
        const val IP_ADDRESS = "ip-address"
    }
}

val Origin.Companion.jsonSerde: JsonSerde.SchemaAware<Origin> get() = OriginJsonSerde