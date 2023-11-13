package org.sollecitom.chassis.correlation.core.serialization.json.access.scope

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.correlation.core.domain.access.scope.AccessContainer
import org.sollecitom.chassis.correlation.core.domain.access.scope.AccessScope
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

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