package com.element.dpg.libs.chassis.nats.client

import com.element.dpg.libs.chassis.core.domain.lifecycle.Stoppable
import io.nats.client.Message

interface NatsPublisher : Stoppable {

    suspend fun publish(message: Message)

    companion object
}



