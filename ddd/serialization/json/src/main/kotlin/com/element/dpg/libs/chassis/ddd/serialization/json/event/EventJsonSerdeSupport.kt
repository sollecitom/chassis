package com.element.dpg.libs.chassis.ddd.serialization.json.event

import kotlinx.datetime.Instant
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.Happening
import com.element.dpg.libs.chassis.ddd.serialization.json.happening.jsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getRequiredInstant
import com.element.dpg.libs.chassis.json.utils.serde.putInstant
import com.element.dpg.libs.chassis.json.utils.serde.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.serde.setValue

interface EventJsonSerdeSupport<EVENT : Event> {

    fun JSONObject.setEventFields(event: EVENT) {

        setValue(Fields.ID, event.id, Id.jsonSerde)
        putInstant(Fields.TIMESTAMP, event.timestamp)
        setValue(Fields.TYPE, event.type, com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde)
        setValue(Fields.CONTEXT, event.context, Event.Context.jsonSerde)
    }

    fun JSONObject.getEventFields(): EventFields {

        val id = getValue(Fields.ID, Id.jsonSerde)
        val timestamp = getRequiredInstant(Fields.TIMESTAMP)
        val type = getValue(Fields.TYPE, com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde)
        val context = getValue(Fields.CONTEXT, Event.Context.jsonSerde)
        return EventFields(id, timestamp, type, context)
    }

    open class EventFields(val id: Id, val timestamp: Instant, val type: Happening.Type, val context: Event.Context) {

        operator fun component1(): Id = id
        operator fun component2(): Instant = timestamp
        operator fun component3(): Happening.Type = type
        operator fun component4(): Event.Context = context
    }

    interface EntitySpecific<EVENT : EntityEvent> : EventJsonSerdeSupport<EVENT> {

        override fun JSONObject.setEventFields(event: EVENT) {

            setValue(EventJsonSerdeSupport.Fields.ID, event.id, Id.jsonSerde)
            putInstant(EventJsonSerdeSupport.Fields.TIMESTAMP, event.timestamp)
            setValue(EventJsonSerdeSupport.Fields.TYPE, event.type, com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde)
            setValue(EventJsonSerdeSupport.Fields.CONTEXT, event.context, Event.Context.jsonSerde)
            setValue(Fields.ENTITY_ID, event.entityId, Id.jsonSerde)
        }

        override fun JSONObject.getEventFields(): EventFields {

            val id = getValue(EventJsonSerdeSupport.Fields.ID, Id.jsonSerde)
            val timestamp = getRequiredInstant(EventJsonSerdeSupport.Fields.TIMESTAMP)
            val type = getValue(EventJsonSerdeSupport.Fields.TYPE, com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde)
            val context = getValue(EventJsonSerdeSupport.Fields.CONTEXT, Event.Context.jsonSerde)
            val entityId = getValue(Fields.ENTITY_ID, Id.jsonSerde)
            return EventFields(id, timestamp, type, context, entityId)
        }

        class EventFields(id: Id, timestamp: Instant, type: Happening.Type, context: Event.Context, val entityId: Id) : EventJsonSerdeSupport.EventFields(id, timestamp, type, context) {

            operator fun component5(): Id = entityId
        }

        private object Fields {
            const val ENTITY_ID = "entity-id"
        }
    }

    private object Fields {
        const val ID = "id"
        const val TIMESTAMP = "timestamp"
        const val TYPE = "type"
        const val CONTEXT = "context"
    }
}