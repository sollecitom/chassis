package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.origin.client.info

import org.json.JSONObject
import com.element.dpg.libs.chassis.json.utils.serde.getRequiredString
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde
import com.element.dpg.libs.chassis.web.client.info.domain.Version

private object VersionJsonSerde : JsonSerde.SchemaAware<Version> {

    private const val SCHEMA_LOCATION = "correlation/access/origin/client/info/Version.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Version) = when (value) {
        is Version.Simple -> SimpleVersionJsonSerde.serialize(value)
        is Version.Semantic -> SemanticVersionJsonSerde.serialize(value)
    }

    override fun deserialize(json: JSONObject) = when (val type = json.getRequiredString(Fields.TYPE)) {
        SimpleVersionJsonSerde.TYPE -> SimpleVersionJsonSerde.deserialize(json)
        SemanticVersionJsonSerde.TYPE -> SemanticVersionJsonSerde.deserialize(json)
        else -> error("Unknown version type '$type'")
    }

    private object Fields {
        const val TYPE = "type"
    }
}

val Version.Companion.jsonSerde: JsonSerde.SchemaAware<Version> get() = VersionJsonSerde