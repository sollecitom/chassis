package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.origin.client.info

import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.json.utils.serde.getRequiredString
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde
import com.element.dpg.libs.chassis.web.client.info.domain.Version
import org.json.JSONObject

internal object SimpleVersionJsonSerde : JsonSerde.SchemaAware<Version.Simple> {

    const val TYPE = "simple"
    private const val SCHEMA_LOCATION = "correlation/access/origin/client/info/SimpleVersion.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Version.Simple) = JSONObject().apply {
        put(Fields.TYPE, TYPE)
        put(Fields.VALUE, value.value.value)
    }

    override fun deserialize(json: JSONObject): Version.Simple {

        val value = json.getRequiredString(Fields.VALUE).let(::Name)
        return Version.Simple(value)
    }

    private object Fields {
        const val TYPE = "type"
        const val VALUE = "value"
    }
}