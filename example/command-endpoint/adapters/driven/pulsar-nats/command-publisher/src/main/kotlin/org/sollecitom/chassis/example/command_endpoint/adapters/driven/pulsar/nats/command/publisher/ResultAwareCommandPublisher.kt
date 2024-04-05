package org.sollecitom.chassis.example.command_endpoint.adapters.driven.pulsar.nats.command.publisher

import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.ddd.domain.CommandResultSubscriber
import org.sollecitom.chassis.ddd.domain.ReceivedCommandPublisher

// TODO move it somewhere shared
interface ResultAwareCommandPublisher : ReceivedCommandPublisher<Command<*, *>, Access>, CommandResultSubscriber {

    companion object
}