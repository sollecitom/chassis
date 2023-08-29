package org.sollecitom.chassis.correlation.core.serialization.json.access.actor

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.actor.ActorOnBehalf
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.serialization.json.access.authentication.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

internal object ActorOnBehalfJsonSerde : JsonSerde.SchemaAware<ActorOnBehalf> {

    const val TYPE_VALUE = "on-behalf"

    override val schema: Schema by lazy { jsonSchemaAt("access/actor/ActorOnBehalf.json") }

    override fun serialize(value: ActorOnBehalf) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        put(Fields.ACCOUNT, Actor.Account.jsonSerde.serialize(value.account))
        put(Fields.BENEFITING_ACCOUNT, Actor.Account.jsonSerde.serialize(value.benefitingAccount))
        put(Fields.AUTHENTICATION, Authentication.jsonSerde.serialize(value.authentication))
    }

    override fun deserialize(json: JSONObject): ActorOnBehalf {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val account = json.getRequiredJSONObject(Fields.ACCOUNT).let(Actor.Account.jsonSerde::deserialize)
        val benefitingAccount = json.getRequiredJSONObject(Fields.BENEFITING_ACCOUNT).let(Actor.Account.jsonSerde::deserialize)
        val authentication = json.getRequiredJSONObject(Fields.AUTHENTICATION).let(Authentication.jsonSerde::deserialize)
        return ActorOnBehalf(account = account, benefitingAccount = benefitingAccount, authentication = authentication)
    }

    private object Fields {
        const val TYPE = "type"
        const val ACCOUNT = "account"
        const val BENEFITING_ACCOUNT = "benefiting-account"
        const val AUTHENTICATION = "authentication"
    }
}

val ActorOnBehalf.Companion.jsonSerde: JsonSerde.SchemaAware<ActorOnBehalf> get() = ActorOnBehalfJsonSerde