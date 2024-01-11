package org.sollecitom.chassis.nats.client

import io.nats.client.Message
import io.nats.client.Options
import kotlinx.coroutines.flow.Flow
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable

interface NatsConsumer : Stoppable {

    val messages: Flow<Message>

    companion object
}