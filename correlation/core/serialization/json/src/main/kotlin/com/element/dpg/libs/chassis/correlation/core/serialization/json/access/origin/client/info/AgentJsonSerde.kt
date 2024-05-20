package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.origin.client.info

import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.json.utils.getStringOrNull
import com.element.dpg.libs.chassis.json.utils.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getValueOrNull
import com.element.dpg.libs.chassis.json.utils.serde.setValue
import com.element.dpg.libs.chassis.web.client.info.domain.Agent
import com.element.dpg.libs.chassis.web.client.info.domain.Version
import org.json.JSONObject

private object AgentJsonSerde : JsonSerde.SchemaAware<Agent> {

    private const val SCHEMA_LOCATION = "correlation/access/origin/client/info/Agent.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Agent) = JSONObject().apply {
        value.className?.value?.let { put(Fields.CLASS_NAME, it) }
        value.name?.value?.let { put(Fields.NAME, it) }
        value.version?.let { setValue(Fields.VERSION, it, Version.jsonSerde) }
    }

    override fun deserialize(json: JSONObject): Agent {

        val className = json.getStringOrNull(Fields.CLASS_NAME)?.let(::Name)
        val name = json.getStringOrNull(Fields.NAME)?.let(::Name)
        val version = json.getValueOrNull(Fields.VERSION, Version.jsonSerde)
        return Agent(className, name, version)
    }

    private object Fields {
        const val CLASS_NAME = "class-name"
        const val NAME = "name"
        const val VERSION = "version"
    }
}

val Agent.Companion.jsonSerde: JsonSerde.SchemaAware<Agent> get() = AgentJsonSerde