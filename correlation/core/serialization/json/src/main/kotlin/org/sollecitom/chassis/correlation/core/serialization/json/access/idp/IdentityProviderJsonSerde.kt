package org.sollecitom.chassis.correlation.core.serialization.json.access.idp

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.correlation.core.domain.access.idp.IdentityProvider
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.serialization.json.tenancy.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

private object IdentityProviderJsonSerde : JsonSerde.SchemaAware<IdentityProvider> {

    private const val SCHEMA_LOCATION = "correlation/access/idp/IdentityProvider.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: IdentityProvider) = JSONObject().apply {
        setValue(Fields.TENANT, value.tenant, Tenant.jsonSerde)
        put(Fields.NAME, value.name.value)
    }

    override fun deserialize(json: JSONObject): IdentityProvider {

        val tenant = json.getValue(Fields.TENANT, Tenant.jsonSerde)
        val name = json.getRequiredString(Fields.NAME).let(::Name)
        return IdentityProvider(name = name, tenant = tenant)
    }

    private object Fields {
        const val TENANT = "tenant"
        const val NAME = "name"
    }
}

val IdentityProvider.Companion.jsonSerde: JsonSerde.SchemaAware<IdentityProvider> get() = IdentityProviderJsonSerde