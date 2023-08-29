package org.sollecitom.chassis.correlation.core.serialization.json.access.actor

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.actor.ImpersonatingActor
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.serialization.json.access.authentication.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

internal object ImpersonatingActorJsonSerde : JsonSerde.SchemaAware<ImpersonatingActor> {

    const val TYPE_VALUE = "impersonating"

    override val schema: Schema by lazy { jsonSchemaAt("access/actor/ImpersonatingActor.json") }

    override fun serialize(value: ImpersonatingActor) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        put(Fields.IMPERSONATOR, Actor.Account.jsonSerde.serialize(value.impersonator))
        put(Fields.IMPERSONATED, Actor.Account.jsonSerde.serialize(value.impersonated))
        put(Fields.AUTHENTICATION, Authentication.jsonSerde.serialize(value.authentication))
    }

    override fun deserialize(json: JSONObject): ImpersonatingActor {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val impersonator = json.getRequiredJSONObject(Fields.IMPERSONATOR).let(Actor.Account.jsonSerde::deserialize)
        val impersonated = json.getRequiredJSONObject(Fields.IMPERSONATED).let(Actor.Account.jsonSerde::deserialize)
        val authentication = json.getRequiredJSONObject(Fields.AUTHENTICATION).let(Authentication.jsonSerde::deserialize)
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