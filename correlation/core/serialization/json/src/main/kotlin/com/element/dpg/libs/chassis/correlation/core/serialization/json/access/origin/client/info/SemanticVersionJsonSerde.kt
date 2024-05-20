package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.origin.client.info

import com.element.dpg.libs.chassis.json.utils.serde.getRequiredInt
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde
import com.element.dpg.libs.chassis.web.client.info.domain.Version
import org.json.JSONObject

internal object SemanticVersionJsonSerde : JsonSerde.SchemaAware<Version.Semantic> {

    const val TYPE = "semantic"
    private const val SCHEMA_LOCATION = "correlation/access/origin/client/info/SemanticVersion.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Version.Semantic) = JSONObject().apply {
        put(Fields.TYPE, TYPE)
        put(Fields.MAJOR, value.major)
        put(Fields.MINOR, value.minor)
        put(Fields.PATCH, value.patch)
    }

    override fun deserialize(json: JSONObject): Version.Semantic {

        val major = json.getRequiredInt(Fields.MAJOR)
        val minor = json.getRequiredInt(Fields.MINOR)
        val patch = json.getRequiredInt(Fields.PATCH)
        return Version.Semantic(major, minor, patch)
    }

    private object Fields {
        const val TYPE = "type"
        const val MAJOR = "major"
        const val MINOR = "minor"
        const val PATCH = "patch"
    }
}