package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.actor

import com.element.dpg.libs.chassis.correlation.core.domain.access.actor.Actor
import com.element.dpg.libs.chassis.json.utils.getRequiredString
import com.element.dpg.libs.chassis.json.utils.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import org.json.JSONObject

private object ActorAccountJsonSerde : JsonSerde.SchemaAware<Actor.Account> {

    private const val SCHEMA_LOCATION = "correlation/access/actor/Account.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

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