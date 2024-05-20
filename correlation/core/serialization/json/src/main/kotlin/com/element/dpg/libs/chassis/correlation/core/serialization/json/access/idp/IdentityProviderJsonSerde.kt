package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.idp

import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.correlation.core.domain.access.customer.Customer
import com.element.dpg.libs.chassis.correlation.core.domain.access.idp.IdentityProvider
import com.element.dpg.libs.chassis.correlation.core.domain.tenancy.Tenant
import com.element.dpg.libs.chassis.correlation.core.serialization.json.customer.jsonSerde
import com.element.dpg.libs.chassis.correlation.core.serialization.json.tenancy.jsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getRequiredString
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.serde.setValue
import org.json.JSONObject

private object IdentityProviderJsonSerde : JsonSerde.SchemaAware<IdentityProvider> {

    private const val SCHEMA_LOCATION = "correlation/access/idp/IdentityProvider.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: IdentityProvider) = JSONObject().apply {
        setValue(Fields.CUSTOMER, value.customer, Customer.jsonSerde)
        setValue(Fields.TENANT, value.tenant, Tenant.jsonSerde)
        put(Fields.NAME, value.name.value)
    }

    override fun deserialize(json: JSONObject): IdentityProvider {

        val customer = json.getValue(Fields.CUSTOMER, Customer.jsonSerde)
        val tenant = json.getValue(Fields.TENANT, Tenant.jsonSerde)
        val name = json.getRequiredString(Fields.NAME).let(::Name)
        return IdentityProvider(name = name, customer = customer, tenant = tenant)
    }

    private object Fields {
        const val CUSTOMER = "customer"
        const val TENANT = "tenant"
        const val NAME = "name"
    }
}

val IdentityProvider.Companion.jsonSerde: JsonSerde.SchemaAware<IdentityProvider> get() = IdentityProviderJsonSerde