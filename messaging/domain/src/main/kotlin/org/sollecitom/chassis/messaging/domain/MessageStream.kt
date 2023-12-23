package org.sollecitom.chassis.messaging.domain

import kotlinx.coroutines.flow.Flow
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.logging.utils.log
import org.sollecitom.chassis.logger.core.loggable.Loggable

interface MessageStream<VALUE> : Startable, Stoppable {

    val messages: Flow<ReceivedMessage<VALUE>>

    suspend fun publish(message: Message<VALUE>): Message.Id

    companion object
}

// TODO move
interface EventStream<EVENT : Event> : MessageStream<EVENT> {

    suspend fun publish(event: EVENT, context: Message.Context = Message.Context()): Message.Id

    companion object
}

suspend fun <EVENT : Event> EventStream<EVENT>.publish(event: EVENT, parentMessage: ReceivedMessage<EVENT>) = publish(event, parentMessage.forkContext())

fun <EVENT : Event> MessageStream<EVENT>.asEventStream(messageProperties: (EVENT) -> Map<String, String> = { emptyMap() }, messageKey: (EVENT) -> String): EventStream<EVENT> = EventStreamAdapter(this, messageKey, messageProperties)

// TODO move
private class EventStreamAdapter<EVENT : Event>(private val delegate: MessageStream<EVENT>, private val messageKey: (EVENT) -> String, private val messageProperties: (EVENT) -> Map<String, String>) : EventStream<EVENT>, MessageStream<EVENT> by delegate {

    override suspend fun publish(event: EVENT, context: Message.Context): Message.Id {

        val message = OutboundMessage(messageKey(event), event, messageProperties(event), context)
        val messageId = delegate.publish(message)
        with(event.context) { logger.log { "Published message with ID '${messageId}' for event with ID '${event.id.stringValue}'" } }
        return messageId
    }

    companion object : Loggable()
}