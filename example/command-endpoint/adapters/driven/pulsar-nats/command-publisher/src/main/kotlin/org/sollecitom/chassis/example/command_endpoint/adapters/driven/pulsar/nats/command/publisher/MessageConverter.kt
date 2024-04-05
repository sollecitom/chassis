package org.sollecitom.chassis.example.command_endpoint.adapters.driven.pulsar.nats.command.publisher

import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.messaging.domain.Message

// TODO move somewhere common
interface MessageConverter<EVENT : Event> {

    fun toMessage(event: EVENT): Message<EVENT>
}