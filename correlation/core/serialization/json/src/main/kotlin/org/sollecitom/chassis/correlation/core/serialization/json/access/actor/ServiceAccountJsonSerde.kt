package org.sollecitom.chassis.correlation.core.serialization.json.access.actor

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.correlation.core.serialization.json.tenancy.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

internal object ServiceAccountJsonSerde : JsonSerde.SchemaAware<Actor.ServiceAccount> {

    const val TYPE_VALUE = "service"

    override val schema: Schema by lazy { jsonSchemaAt("access/actor/ServiceAccount.json") }

    override fun serialize(value: Actor.ServiceAccount) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        put(Fields.ID, Id.jsonSerde.serialize(value.id))
        put(Fields.TENANT, Tenant.jsonSerde.serialize(value.tenant))
    }

    override fun deserialize(json: JSONObject): Actor.ServiceAccount {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val id = json.getRequiredJSONObject(Fields.ID).let(Id.jsonSerde::deserialize)
        val tenant = json.getRequiredJSONObject(Fields.TENANT).let(Tenant.jsonSerde::deserialize)
        return Actor.ServiceAccount(id = id, tenant = tenant)
    }

    private object Fields {
        const val TYPE = "type"
        const val ID = "id"
        const val TENANT = "tenant"
    }
}

val Actor.ServiceAccount.Companion.jsonSerde: JsonSerde.SchemaAware<Actor.ServiceAccount> get() = ServiceAccountJsonSerde