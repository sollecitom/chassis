package com.element.dpg.libs.chassis.ddd.serialization.json.event

import com.element.dpg.libs.chassis.correlation.core.domain.context.InvocationContext
import com.element.dpg.libs.chassis.correlation.core.serialization.json.context.jsonSerde
import com.element.dpg.libs.chassis.ddd.domain.Event
import com.element.dpg.libs.chassis.json.utils.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.getValueOrNull
import com.element.dpg.libs.chassis.json.utils.serde.setValue
import org.json.JSONObject

internal object EventContextJsonSerde : JsonSerde.SchemaAware<Event.Context> {

    private const val SCHEMA_LOCATION = "ddd/event/EventContext.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Event.Context) = JSONObject().apply {
        setValue(Fields.INVOCATION, value.invocation, InvocationContext.jsonSerde)
        value.parent?.let { setValue(Fields.PARENT, it, Event.Reference.jsonSerde) }
        value.originating?.let { setValue(Fields.ORIGINATING, it, Event.Reference.jsonSerde) }
    }

    override fun deserialize(json: JSONObject): Event.Context {

        val invocation = json.getValue(Fields.INVOCATION, InvocationContext.jsonSerde)
        val parent = json.getValueOrNull(Fields.PARENT, Event.Reference.jsonSerde)
        val originating = json.getValueOrNull(Fields.ORIGINATING, Event.Reference.jsonSerde)
        return Event.Context(invocation, parent, originating)
    }

    private object Fields {
        const val INVOCATION = "invocation"
        const val PARENT = "parent"
        const val ORIGINATING = "originating"
    }
}

val Event.Context.Companion.jsonSerde: JsonSerde.SchemaAware<Event.Context> get() = EventContextJsonSerde