package org.sollecitom.chassis.correlation.core.serialization.json.access.actor

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

private object ActorAccountJsonSerde : JsonSerde.SchemaAware<Actor.Account> {

    override val schema: Schema by lazy { jsonSchemaAt("access/actor/Account.json") }

    override fun serialize(value: Actor.Account) = when (value) {
        is Actor.UserAccount -> Actor.UserAccount.jsonSerde.serialize(value)
        is Actor.ServiceAccount -> Actor.ServiceAccount.jsonSerde.serialize(value)
    }

    override fun deserialize(json: JSONObject) = when (val type = json.getRequiredString(Fields.TYPE)) {
        UserAccountJsonSerde.TYPE_VALUE -> Actor.UserAccount.jsonSerde.deserialize(json)
        ServiceAccountJsonSerde.TYPE_VALUE -> Actor.ServiceAccount.jsonSerde.deserialize(json)
        else -> error("Unsupported actor account type $type")
    }

    private object Fields {
        const val TYPE = "type"
    }
}

val Actor.Account.Companion.jsonSerde: JsonSerde.SchemaAware<Actor.Account> get() = ActorAccountJsonSerde