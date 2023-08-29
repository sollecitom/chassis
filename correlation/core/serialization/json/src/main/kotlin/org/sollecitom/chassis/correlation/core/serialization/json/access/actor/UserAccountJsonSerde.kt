package org.sollecitom.chassis.correlation.core.serialization.json.access.actor

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.fromString
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.serialization.json.tenancy.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

internal object UserAccountJsonSerde : JsonSerde.SchemaAware<Actor.UserAccount> {

    const val TYPE_VALUE = "user"

    override val schema: Schema by lazy { jsonSchemaAt("access/actor/UserAccount.json") }

    override fun serialize(value: Actor.UserAccount) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        put(Fields.ID, value.id.stringValue)
        put(Fields.TENANT, Tenant.jsonSerde.serialize(value.tenant))
    }

    override fun deserialize(json: JSONObject): Actor.UserAccount {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val id = json.getRequiredString(Fields.ID).let(Id.Companion::fromString)
        val tenant = json.getRequiredJSONObject(Fields.TENANT).let(Tenant.jsonSerde::deserialize)
        return Actor.UserAccount(id = id, tenant = tenant)
    }

    private object Fields {
        const val TYPE = "type"
        const val ID = "id"
        const val TENANT = "tenant"
    }
}

val Actor.UserAccount.Companion.jsonSerde: JsonSerde.SchemaAware<Actor.UserAccount> get() = UserAccountJsonSerde