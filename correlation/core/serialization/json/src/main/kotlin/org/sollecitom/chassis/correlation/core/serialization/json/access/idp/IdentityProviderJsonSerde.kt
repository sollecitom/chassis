package org.sollecitom.chassis.correlation.core.serialization.json.access.idp

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.correlation.core.domain.access.idp.IdentityProvider
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.serialization.json.tenancy.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

private object IdentityProviderJsonSerde : JsonSerde.SchemaAware<IdentityProvider> {

    override val schema: Schema by lazy { jsonSchemaAt("access/idp/IdentityProvider.json") }

    override fun serialize(value: IdentityProvider) = JSONObject().apply {
        put(Fields.TENANT, Tenant.jsonSerde.serialize(value.tenant))
        put(Fields.NAME, value.name.value)
    }

    override fun deserialize(json: JSONObject): IdentityProvider {

        val tenant = json.getRequiredJSONObject(Fields.TENANT).let(Tenant.jsonSerde::deserialize)
        val name = json.getRequiredString(Fields.NAME).let(::Name)
        return IdentityProvider(name = name, tenant = tenant)
    }

    private object Fields {
        const val TENANT = "tenant"
        const val NAME = "name"
    }
}

val IdentityProvider.Companion.jsonSerde: JsonSerde.SchemaAware<IdentityProvider> get() = IdentityProviderJsonSerde