package org.sollecitom.chassis.correlation.core.serialization.json.customer

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.correlation.core.domain.access.customer.Customer
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

private object CustomerJsonSerde : JsonSerde.SchemaAware<Customer> {

    private const val SCHEMA_LOCATION = "correlation/customer/Customer.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Customer) = JSONObject().apply {
        setValue(Fields.ID, value.id, Id.jsonSerde)
    }

    override fun deserialize(json: JSONObject): Customer {

        val id = json.getValue(Fields.ID, Id.jsonSerde)
        return Customer(id = id)
    }

    private object Fields {
        const val ID = "id"
    }
}

val Customer.Companion.jsonSerde: JsonSerde.SchemaAware<Customer> get() = CustomerJsonSerde