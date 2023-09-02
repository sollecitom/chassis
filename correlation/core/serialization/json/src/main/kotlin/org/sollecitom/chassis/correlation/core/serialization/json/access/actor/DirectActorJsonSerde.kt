package org.sollecitom.chassis.correlation.core.serialization.json.access.actor

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.actor.DirectActor
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.serialization.json.access.authentication.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

internal object DirectActorJsonSerde : JsonSerde.SchemaAware<DirectActor> {

    const val TYPE_VALUE = "direct"
    private const val SCHEMA_LOCATION = "correlation/access/actor/DirectActor.json"
    override val schema: Schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: DirectActor) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ACCOUNT, value.account, Actor.Account.jsonSerde)
        setValue(Fields.AUTHENTICATION, value.authentication, Authentication.jsonSerde)
    }

    override fun deserialize(json: JSONObject): DirectActor {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val account = json.getValue(Fields.ACCOUNT, Actor.Account.jsonSerde)
        val authentication = json.getValue(Fields.AUTHENTICATION, Authentication.jsonSerde)
        return DirectActor(account = account, authentication = authentication)
    }

    private object Fields {
        const val TYPE = "type"
        const val ACCOUNT = "account"
        const val AUTHENTICATION = "authentication"
    }
}

val DirectActor.Companion.jsonSerde: JsonSerde.SchemaAware<DirectActor> get() = DirectActorJsonSerde