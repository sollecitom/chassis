package org.sollecitom.chassis.example.command_endpoint.adapters.driven.pulsar.nats.command.publisher

import org.sollecitom.chassis.ddd.domain.CommandWasReceived
import org.sollecitom.chassis.messaging.domain.Message
import org.sollecitom.chassis.messaging.domain.OutboundMessage

internal object CommandWasReceivedMessageConverter : MessageConverter<CommandWasReceived<*>> {

    override fun toMessage(event: CommandWasReceived<*>): Message<CommandWasReceived<*>> {

        // TODO switch based on command type and think about key and properties
        return OutboundMessage(key = event.context.invocation.idempotency.id().value, value = event, properties = emptyMap(), context = Message.Context())
    }
}