package org.sollecitom.chassis.messaging.domain

import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.core.domain.naming.Name

interface MessageProducer<in VALUE> : Startable, Stoppable, AutoCloseable {

    val name: Name
    val topic: Topic

    suspend fun produce(message: Message<VALUE>): Message.Id
}