package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.scope

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.correlation.core.domain.access.scope.AccessContainer
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.serde.setValue

private object AccessContainerJsonSerde : JsonSerde.SchemaAware<AccessContainer> {

    private const val SCHEMA_LOCATION = "correlation/access/scope/AccessContainer.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: AccessContainer) = JSONObject().apply {
        setValue(Fields.ID, value.id, Id.jsonSerde)
    }

    override fun deserialize(json: JSONObject): AccessContainer {

        val id = json.getValue(Fields.ID, Id.jsonSerde)
        return AccessContainer(id = id)
    }

    private object Fields {
        const val ID = "id"
    }
}

val AccessContainer.Companion.jsonSerde: JsonSerde.SchemaAware<AccessContainer> get() = AccessContainerJsonSerde