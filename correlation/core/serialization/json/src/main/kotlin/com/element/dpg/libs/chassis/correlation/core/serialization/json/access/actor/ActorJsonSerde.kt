package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.actor

import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.actor.ActorOnBehalf
import org.sollecitom.chassis.correlation.core.domain.access.actor.DirectActor
import org.sollecitom.chassis.correlation.core.domain.access.actor.ImpersonatingActor
import com.element.dpg.libs.chassis.json.utils.serde.getRequiredString
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde

private object ActorJsonSerde : JsonSerde.SchemaAware<Actor> {

    private const val SCHEMA_LOCATION = "correlation/access/actor/Actor.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

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