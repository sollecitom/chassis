package org.sollecitom.chassis.example.command_endpoint.adapters.driven.pulsar.nats.command.publisher

import kotlinx.coroutines.Deferred
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.domain.CommandResultSubscriber
import org.sollecitom.chassis.ddd.domain.CommandWasReceived
import org.sollecitom.chassis.ddd.domain.ReceivedCommandPublisher
import org.sollecitom.chassis.example.command_endpoint.domain.user.registration.RegisterUser

class PulsarNatsCommandPublisher : ReceivedCommandPublisher<RegisterUser, Access>, CommandResultSubscriber<RegisterUser, RegisterUser.Result, Access>, Startable, Stoppable {

    override suspend fun start() {
        TODO("Not yet implemented")
    }

    override suspend fun stop() {
        TODO("Not yet implemented")
    }

    context(InvocationContext<Access>)
    override fun resultForCommand(event: CommandWasReceived<RegisterUser>): Deferred<RegisterUser.Result> {
        TODO("Not yet implemented")
    }

    context(InvocationContext<Access>)
    override suspend fun publish(event: CommandWasReceived<RegisterUser>) {
        TODO("Not yet implemented")
    }
}