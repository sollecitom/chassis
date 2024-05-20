package com.element.dpg.libs.chassis.messaging.domain

import com.element.dpg.libs.chassis.core.domain.lifecycle.Stoppable
import com.element.dpg.libs.chassis.core.domain.naming.Name

interface MessageProducer<in VALUE> : Stoppable, AutoCloseable {

    val name: Name

    suspend fun produce(message: Message<VALUE>, topic: Topic): Message.Id
}