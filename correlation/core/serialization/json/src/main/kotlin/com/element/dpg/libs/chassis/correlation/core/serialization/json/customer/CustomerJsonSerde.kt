package com.element.dpg.libs.chassis.correlation.core.serialization.json.customer

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.correlation.core.domain.access.customer.Customer
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.serde.setValue

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