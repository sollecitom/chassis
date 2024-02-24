package org.sollecitom.chassis.messaging.domain

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.core.domain.naming.Name

interface MessageConsumer<out VALUE> : Stoppable, AutoCloseable {

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