package org.sollecitom.chassis.example.command_endpoint.adapters.driven.pulsar.nats.command.publisher

import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.ddd.domain.CommandResultSubscriber
import org.sollecitom.chassis.ddd.domain.ReceivedCommandPublisher

internal class CompositeResultAwareCommandPublisher(private val publisher: ReceivedCommandPublisher<Command<*, *>, Access>, private val subscriber: CommandResultSubscriber) : ResultAwareCommandPublisher, ReceivedCommandPublisher<Command<*, *>, Access> by publisher, CommandResultSubscriber by subscriber {

    override suspend fun start() {

        subscriber.start()
        publisher.start()
    }

    override suspend fun stop() {

        publisher.stop()
        subscriber.stop()
    }
}