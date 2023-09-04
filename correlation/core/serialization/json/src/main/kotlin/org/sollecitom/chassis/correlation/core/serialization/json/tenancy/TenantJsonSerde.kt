package org.sollecitom.chassis.correlation.core.serialization.json.tenancy

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

private object TenantJsonSerde : JsonSerde.SchemaAware<Tenant> {

    private const val SCHEMA_LOCATION = "correlation/tenancy/Tenant.json"
    override val schema: Schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Tenant) = JSONObject().apply {
        setValue(Fields.ID, value.id, Id.jsonSerde)
    }

    override fun deserialize(json: JSONObject): Tenant {

        val id = json.getValue(Fields.ID, Id.jsonSerde)
        return Tenant(id = id)
    }

    private object Fields {
        const val ID = "id"
    }
}

val Tenant.Companion.jsonSerde: JsonSerde.SchemaAware<Tenant> get() = TenantJsonSerde