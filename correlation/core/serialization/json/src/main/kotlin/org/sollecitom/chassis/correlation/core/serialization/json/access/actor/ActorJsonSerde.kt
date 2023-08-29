package org.sollecitom.chassis.correlation.core.serialization.json.access.actor

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.actor.ActorOnBehalf
import org.sollecitom.chassis.correlation.core.domain.access.actor.DirectActor
import org.sollecitom.chassis.correlation.core.domain.access.actor.ImpersonatingActor
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

private object ActorJsonSerde : JsonSerde.SchemaAware<Actor> {

    override val schema: Schema by lazy { jsonSchemaAt("access/actor/Actor.json") }

    override fun serialize(value: Actor) = when (value) {
        is DirectActor -> DirectActor.jsonSerde.serialize(value)
        is ActorOnBehalf -> ActorOnBehalf.jsonSerde.serialize(value)
        is ImpersonatingActor -> ImpersonatingActor.jsonSerde.serialize(value)
    }

    override fun deserialize(json: JSONObject) = when (val type = json.getRequiredString(Fields.TYPE)) {
        DirectActorJsonSerde.TYPE_VALUE -> DirectActor.jsonSerde.deserialize(json)
        ActorOnBehalfJsonSerde.TYPE_VALUE -> ActorOnBehalf.jsonSerde.deserialize(json)
        ImpersonatingActorJsonSerde.TYPE_VALUE -> ImpersonatingActor.jsonSerde.deserialize(json)
        else -> error("Unsupported actor type $type")
    }

    private object Fields {
        const val TYPE = "type"
    }
}

val Actor.Companion.jsonSerde: JsonSerde.SchemaAware<Actor> get() = ActorJsonSerde