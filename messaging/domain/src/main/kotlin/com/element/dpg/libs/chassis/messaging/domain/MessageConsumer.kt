package com.element.dpg.libs.chassis.messaging.domain

import com.element.dpg.libs.chassis.core.domain.lifecycle.Startable
import com.element.dpg.libs.chassis.core.domain.lifecycle.Stoppable
import com.element.dpg.libs.chassis.core.domain.naming.Name
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

interface MessageConsumer<out VALUE> : Startable, Stoppable, AutoCloseable {

    val name: Name
    val subscriptionName: Name
    val topics: Set<Topic>

    suspend fun receive(): ReceivedMessage<VALUE>
}

val <VALUE> MessageConsumer<VALUE>.messages: Flow<ReceivedMessage<VALUE>>
    get() = flow {
        while (currentCoroutineContext().isActive) {
            val message = receive()
            emit(message)
        }
    }