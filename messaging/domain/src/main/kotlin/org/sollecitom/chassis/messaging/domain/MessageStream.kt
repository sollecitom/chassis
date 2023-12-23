package org.sollecitom.chassis.messaging.domain

import kotlinx.coroutines.flow.Flow
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.ddd.domain.Event

interface MessageStream<VALUE> : Startable, Stoppable {

    val messages: Flow<ReceivedMessage<VALUE>>

    suspend fun publish(message: Message<VALUE>): Message.Id

    companion object
}
