package org.sollecitom.chassis.correlation.core.serialization.json

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.fromString
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

private object TenantJsonSerde : JsonSerde.SchemaAware<Tenant> {

    override val schema: Schema by lazy { jsonSchemaAt("Tenant.json") }

    override fun serialize(value: Tenant) = JSONObject().apply {
        put(Fields.ID, value.id.stringValue)
    }

    override fun deserialize(json: JSONObject): Tenant {

        val id = json.getString(Fields.ID).let(Id.Companion::fromString)
        return Tenant(id = id)
    }

    private object Fields {
        const val ID = "id"
    }
}

val Tenant.Companion.jsonSerde: JsonSerde.SchemaAware<Tenant> get() = TenantJsonSerde