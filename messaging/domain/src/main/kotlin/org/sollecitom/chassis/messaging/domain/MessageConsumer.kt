package org.sollecitom.chassis.messaging.domain

import kotlinx.coroutines.flow.Flow
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.core.domain.naming.Name

interface MessageConsumer<out VALUE> : Stoppable, AutoCloseable {

    val name: Name
    val subscriptionName: Name
    val topics: Set<Topic>

    suspend fun receive(): ReceivedMessage<VALUE>

    val messages: Flow<ReceivedMessage<VALUE>>
}