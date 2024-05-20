package com.element.dpg.libs.chassis.nats.client

import com.element.dpg.libs.chassis.core.domain.lifecycle.Stoppable
import io.nats.client.Message
import kotlinx.coroutines.flow.Flow

interface NatsConsumer : Stoppable {

    val messages: Flow<Message>

    companion object
}