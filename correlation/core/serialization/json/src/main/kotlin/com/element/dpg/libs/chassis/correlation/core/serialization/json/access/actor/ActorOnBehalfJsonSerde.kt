package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.actor

import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.actor.ActorOnBehalf
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.serialization.json.access.authentication.jsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getRequiredString
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.serde.setValue

internal object ActorOnBehalfJsonSerde : JsonSerde.SchemaAware<ActorOnBehalf> {

    const val TYPE_VALUE = "on-behalf"
    private const val SCHEMA_LOCATION = "correlation/access/actor/ActorOnBehalf.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: ActorOnBehalf) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ACCOUNT, value.account, Actor.Account.jsonSerde)
        setValue(Fields.BENEFITING_ACCOUNT, value.benefitingAccount, Actor.Account.jsonSerde)
        setValue(Fields.AUTHENTICATION, value.authentication, Authentication.jsonSerde)
    }

    override fun deserialize(json: JSONObject): ActorOnBehalf {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val account = json.getValue(Fields.ACCOUNT, Actor.Account.jsonSerde)
        val benefitingAccount = json.getValue(Fields.BENEFITING_ACCOUNT, Actor.Account.jsonSerde)
        val authentication = json.getValue(Fields.AUTHENTICATION, Authentication.jsonSerde)
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