package org.sollecitom.chassis.example.command_endpoint.adapters.driven.pulsar.nats.command.publisher

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.SupervisorJob
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema
import org.sollecitom.chassis.core.domain.identity.InstanceInfo
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.domain.CommandResultSubscriber
import org.sollecitom.chassis.ddd.domain.CommandWasReceived
import org.sollecitom.chassis.ddd.domain.ReceivedCommandPublisher
import org.sollecitom.chassis.example.command_endpoint.domain.user.registration.RegisterUser
import org.sollecitom.chassis.example.command_endpoint.domain.user.registration.UserWithPendingRegistration
import org.sollecitom.chassis.messaging.domain.Topic
import org.sollecitom.chassis.pulsar.messaging.adapter.messageProducer

class PulsarNatsCommandPublisher(private val configuration: Configuration, private val scope: CoroutineScope = CoroutineScope(SupervisorJob())) : ReceivedCommandPublisher<RegisterUser, Access>, CommandResultSubscriber<RegisterUser, RegisterUser.Result, Access>, Startable, Stoppable {

    private val pulsarClient: PulsarClient = TODO("")
    private val messageProducer = pulsarClient.messageProducer(configuration.topic, configuration.schema, configuration.instanceInfo)

    override suspend fun start() {
        // TODO
    }

    override suspend fun stop() {

        // TODO
    }

//    configuration.instanceInfo, configuration.outboundTopic, Event.jsonSerde.asPulsarSchema(), brokerURI

    context(InvocationContext<Access>)
    override fun resultForCommand(event: CommandWasReceived<RegisterUser>): Deferred<RegisterUser.Result> {

        // TODO connect to NATS, parse the result, and complete the deferred
        val deferred = CompletableDeferred<RegisterUser.Result>()
//        scope.async {}
        val result = RegisterUser.Result.Accepted(user = UserWithPendingRegistration(StringId("something")))
        return deferred
    }

    context(InvocationContext<Access>)
    override suspend fun publish(event: CommandWasReceived<RegisterUser>) {

        // TODO publish the event to Apache Pulsar
    }

    data class Configuration(val topic: Topic, val schema: Schema<RegisterUser>, val instanceInfo: InstanceInfo) {

        companion object
    }
}