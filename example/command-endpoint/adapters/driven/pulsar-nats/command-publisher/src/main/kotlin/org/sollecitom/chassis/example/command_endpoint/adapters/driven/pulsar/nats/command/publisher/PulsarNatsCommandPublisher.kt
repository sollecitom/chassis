package org.sollecitom.chassis.example.command_endpoint.adapters.driven.pulsar.nats.command.publisher

import kotlinx.coroutines.*
import org.apache.pulsar.client.api.ClientBuilder
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema
import org.sollecitom.chassis.core.domain.identity.InstanceInfo
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.context.targetTenant
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.logging.utils.log
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.ddd.domain.CommandResultSubscriber
import org.sollecitom.chassis.ddd.domain.CommandWasReceived
import org.sollecitom.chassis.ddd.domain.ReceivedCommandPublisher
import org.sollecitom.chassis.example.event.domain.user.registration.RegisterUser
import org.sollecitom.chassis.example.event.domain.user.registration.UserWithPendingRegistration
import org.sollecitom.chassis.example.event.serialization.json.jsonSerde
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.messaging.domain.TenantAgnosticTopic
import org.sollecitom.chassis.pulsar.json.serialization.asPulsarSchema
import org.sollecitom.chassis.pulsar.messaging.adapter.messageProducer
import org.sollecitom.chassis.pulsar.utils.brokerURI
import java.net.URI

private val schema = CommandWasReceived.jsonSerde.asPulsarSchema()

fun commandPublisher(configuration: ResultAwareCommandPublisher.Configuration, messageConverter: MessageConverter<CommandWasReceived<*>> = CommandWasReceivedMessageConverter): ResultAwareCommandPublisher = PulsarNatsCommandPublisher(PulsarPublisher(configuration.pulsarBrokerURI, configuration.topic, configuration.instanceInfo, schema, messageConverter))

interface ResultAwareCommandPublisher : ReceivedCommandPublisher<Command<*, *>, Access>, CommandResultSubscriber, Startable, Stoppable {

    data class Configuration(val pulsarBrokerURI: URI, val topic: TenantAgnosticTopic, val instanceInfo: InstanceInfo) {

        companion object
    }

    companion object
}

internal class PulsarPublisher(private val brokerURI: URI, private val topic: TenantAgnosticTopic, private val instanceInfo: InstanceInfo, private val schema: Schema<CommandWasReceived<*>>, private val messageConverter: MessageConverter<CommandWasReceived<*>>, private val customize: ClientBuilder.() -> ClientBuilder = { this }) : ReceivedCommandPublisher<Command<*, *>, Access>, Startable, Stoppable {

    private val pulsarClient: PulsarClient by lazy { PulsarClient.builder().customize().brokerURI(brokerURI).build() }

    private val messageProducer by lazy { pulsarClient.messageProducer(instanceInfo, schema) }

    override suspend fun start() {}

    override suspend fun stop() {

        messageProducer.stop()
        pulsarClient.close()
    }

    context(InvocationContext<Access>)
    override suspend fun publish(event: CommandWasReceived<Command<*, *>>) {

        val message = messageConverter.toMessage(event)
        val messageId = messageProducer.produce(message, topic.withTenant(targetTenant))
        PulsarNatsCommandPublisher.logger.log { "Produced received command with type ${event.command.type} with message ID $messageId" }
    }

    private fun TenantAgnosticTopic.withTenant(tenant: Tenant) = withTenant(tenant.id.stringValue.let(::Name))

    companion object : Loggable()
}

// TODO make this re-usable, and move it
internal class PulsarNatsCommandPublisher(private val publisher: PulsarPublisher, private val scope: CoroutineScope = CoroutineScope(SupervisorJob())) : ResultAwareCommandPublisher, ReceivedCommandPublisher<Command<*, *>, Access> by publisher {

    override suspend fun start() {

        publisher.start()
    }

    override suspend fun stop() {

        publisher.stop()
    }

    context(InvocationContext<ACCESS>)
    @Suppress("UNCHECKED_CAST")
    override fun <COMMAND : Command<RESULT, ACCESS>, RESULT : Any, ACCESS : Access> resultForCommand(event: CommandWasReceived<COMMAND>): Deferred<RESULT> {

        val deferred = CompletableDeferred<RESULT>()
        //  TODO connect to NATS
        scope.launch {
            // TODO receive from NATS, parse the result, and complete the deferred
            delay(3000)
            val result = RegisterUser.Result.Accepted(user = UserWithPendingRegistration(StringId("something"))) // TODO fix this by reading the type from the message and by using a deserializer
            deferred.complete(result as RESULT)
        }
        return deferred
    }

    companion object : Loggable()
}
