package org.sollecitom.chassis.correlation.core.serialization.json.access.actor

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.correlation.core.serialization.json.tenancy.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

internal object ServiceAccountJsonSerde : JsonSerde.SchemaAware<Actor.ServiceAccount> {

    const val TYPE_VALUE = "service"
    private const val SCHEMA_LOCATION = "correlation/access/actor/ServiceAccount.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Actor.ServiceAccount) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ID, value.id, Id.jsonSerde)
        setValue(Fields.TENANT, value.tenant, Tenant.jsonSerde)
    }

    override fun deserialize(json: JSONObject): Actor.ServiceAccount {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val id = json.getValue(Fields.ID, Id.jsonSerde)
        val tenant = json.getValue(Fields.TENANT, Tenant.jsonSerde)
        return Actor.ServiceAccount(id = id, tenant = tenant)
    }

    private object Fields {
        const val TYPE = "type"
        const val ID = "id"
        const val TENANT = "tenant"
    }
}

val Actor.ServiceAccount.Companion.jsonSerde: JsonSerde.SchemaAware<Actor.ServiceAccount> get() = ServiceAccountJsonSerde