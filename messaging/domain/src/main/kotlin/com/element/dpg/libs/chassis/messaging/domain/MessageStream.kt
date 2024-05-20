package com.element.dpg.libs.chassis.messaging.domain

import com.element.dpg.libs.chassis.core.domain.lifecycle.Startable
import com.element.dpg.libs.chassis.core.domain.lifecycle.Stoppable
import kotlinx.coroutines.flow.Flow

interface MessageStream<VALUE> : Startable, Stoppable {

    val messages: Flow<ReceivedMessage<VALUE>>

    suspend fun publish(message: Message<VALUE>): Message.Id

    companion object
}
