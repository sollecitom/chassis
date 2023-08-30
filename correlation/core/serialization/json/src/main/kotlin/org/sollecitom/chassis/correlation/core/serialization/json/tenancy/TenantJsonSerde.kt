package org.sollecitom.chassis.correlation.core.serialization.json.tenancy

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.fromString
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.serialization.json.access.actor.ServiceAccountJsonSerde
import org.sollecitom.chassis.correlation.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

private object TenantJsonSerde : JsonSerde.SchemaAware<Tenant> {

    override val schema: Schema by lazy { jsonSchemaAt("tenancy/Tenant.json") }

    override fun serialize(value: Tenant) = JSONObject().apply {
        put(Fields.ID, Id.jsonSerde.serialize(value.id))
    }

    override fun deserialize(json: JSONObject): Tenant {

        val id = json.getRequiredJSONObject(Fields.ID).let(Id.jsonSerde::deserialize)
        return Tenant(id = id)
    }

    private object Fields {
        const val ID = "id"
    }
}

val Tenant.Companion.jsonSerde: JsonSerde.SchemaAware<Tenant> get() = TenantJsonSerde