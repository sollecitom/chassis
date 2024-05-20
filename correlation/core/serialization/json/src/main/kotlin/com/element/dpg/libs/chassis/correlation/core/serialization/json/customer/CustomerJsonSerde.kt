package com.element.dpg.libs.chassis.correlation.core.serialization.json.customer

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde
import com.element.dpg.libs.chassis.correlation.core.domain.access.customer.Customer
import com.element.dpg.libs.chassis.json.utils.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.setValue
import org.json.JSONObject

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