package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.actor

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde
import com.element.dpg.libs.chassis.correlation.core.domain.access.actor.Actor
import com.element.dpg.libs.chassis.correlation.core.serialization.json.customer.jsonSerde
import com.element.dpg.libs.chassis.correlation.core.serialization.json.tenancy.jsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getRequiredString
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.serde.setValue
import org.json.JSONObject

internal object ServiceAccountJsonSerde : JsonSerde.SchemaAware<Actor.ServiceAccount> {

    const val TYPE_VALUE = "service"
    private const val SCHEMA_LOCATION = "correlation/access/actor/ServiceAccount.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Actor.ServiceAccount) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ID, value.id, Id.jsonSerde)
        setValue(Fields.CUSTOMER, value.customer, com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde)
        setValue(Fields.TENANT, value.tenant, com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde)
    }

    override fun deserialize(json: JSONObject): Actor.ServiceAccount {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val id = json.getValue(Fields.ID, Id.jsonSerde)
        val customer = json.getValue(Fields.CUSTOMER, com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde)
        val tenant = json.getValue(Fields.TENANT, com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde)
        return Actor.ServiceAccount(id = id, customer = customer, tenant = tenant)
    }

    private object Fields {
        const val TYPE = "type"
        const val ID = "id"
        const val CUSTOMER = "customer"
        const val TENANT = "tenant"
    }
}

val Actor.ServiceAccount.Companion.jsonSerde: JsonSerde.SchemaAware<Actor.ServiceAccount> get() = ServiceAccountJsonSerde