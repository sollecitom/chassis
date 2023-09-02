package org.sollecitom.chassis.correlation.core.serialization.json.access.actor

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.actor.ImpersonatingActor
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.serialization.json.access.authentication.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

internal object ImpersonatingActorJsonSerde : JsonSerde.SchemaAware<ImpersonatingActor> {

    const val TYPE_VALUE = "impersonating"
    private const val SCHEMA_LOCATION = "correlation/access/actor/ImpersonatingActor.json"
    override val schema: Schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: ImpersonatingActor) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.IMPERSONATOR, value.impersonator, Actor.Account.jsonSerde)
        setValue(Fields.IMPERSONATED, value.impersonated, Actor.Account.jsonSerde)
        setValue(Fields.AUTHENTICATION, value.authentication, Authentication.jsonSerde)
    }

    override fun deserialize(json: JSONObject): ImpersonatingActor {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val impersonator = json.getValue(Fields.IMPERSONATOR, Actor.Account.jsonSerde)
        val impersonated = json.getValue(Fields.IMPERSONATED, Actor.Account.jsonSerde)
        val authentication = json.getValue(Fields.AUTHENTICATION, Authentication.jsonSerde)
        return ImpersonatingActor(impersonator = impersonator, impersonated = impersonated, authentication = authentication)
    }

    private object Fields {
        const val TYPE = "type"
        const val IMPERSONATOR = "impersonator"
        const val IMPERSONATED = "impersonated"
        const val AUTHENTICATION = "authentication"
    }
}

val ImpersonatingActor.Companion.jsonSerde: JsonSerde.SchemaAware<ImpersonatingActor> get() = ImpersonatingActorJsonSerde