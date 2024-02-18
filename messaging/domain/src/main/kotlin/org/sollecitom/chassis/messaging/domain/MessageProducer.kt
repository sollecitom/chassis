package org.sollecitom.chassis.messaging.domain

import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.core.domain.naming.Name

interface MessageProducer<in VALUE> : Stoppable, AutoCloseable {

    val name: Name

    suspend fun produce(message: Message<VALUE>, topic: Topic): Message.Id
}