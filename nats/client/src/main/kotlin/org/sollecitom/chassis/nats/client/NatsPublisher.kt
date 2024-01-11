package org.sollecitom.chassis.nats.client

import io.nats.client.Message
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable

interface NatsPublisher : Stoppable {

    suspend fun publish(message: Message)

    companion object
}



