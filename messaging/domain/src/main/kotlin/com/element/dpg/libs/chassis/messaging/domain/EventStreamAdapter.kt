package com.element.dpg.libs.chassis.messaging.domain

import com.element.dpg.libs.chassis.ddd.domain.Event
import com.element.dpg.libs.chassis.ddd.logging.utils.log
import com.element.dpg.libs.chassis.logger.core.loggable.Loggable

private class EventStreamAdapter<EVENT : Event>(private val delegate: MessageStream<EVENT>, private val messageKey: (EVENT) -> String, private val messageProperties: (EVENT) -> Map<String, String>) : EventStream<EVENT>, MessageStream<EVENT> by delegate {

    override suspend fun publish(event: EVENT, context: Message.Context): Message.Id {

        val message = OutboundMessage(messageKey(event), event, messageProperties(event), context)
        val messageId = delegate.publish(message)
        with(event.context) { logger.log { "Published message with ID '${messageId}' for event with ID '${event.id.stringValue}'" } }
        return messageId
    }

    companion object : Loggable()
}

fun <EVENT : Event> MessageStream<EVENT>.asEventStream(messageProperties: (EVENT) -> Map<String, String> = { emptyMap() }, messageKey: (EVENT) -> String): EventStream<EVENT> = EventStreamAdapter(this, messageKey, messageProperties)