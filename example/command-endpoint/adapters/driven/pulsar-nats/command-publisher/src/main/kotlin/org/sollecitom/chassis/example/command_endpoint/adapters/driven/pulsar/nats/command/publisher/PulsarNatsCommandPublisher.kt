package org.sollecitom.chassis.example.command_endpoint.adapters.driven.pulsar.nats.command.publisher

import kotlinx.coroutines.*
import org.apache.pulsar.client.api.ClientBuilder
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema
import org.sollecitom.chassis.core.domain.identity.InstanceInfo
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.logging.utils.log
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.ddd.domain.CommandResultSubscriber
import org.sollecitom.chassis.ddd.domain.CommandWasReceived
import org.sollecitom.chassis.ddd.domain.ReceivedCommandPublisher
import org.sollecitom.chassis.example.event.domain.user.registration.RegisterUser
import org.sollecitom.chassis.example.event.domain.user.registration.UserWithPendingRegistration
import org.sollecitom.chassis.example.event.serialization.json.jsonSerde
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.messaging.domain.Message
import org.sollecitom.chassis.messaging.domain.OutboundMessage
import org.sollecitom.chassis.messaging.domain.Topic
import org.sollecitom.chassis.pulsar.json.serialization.asPulsarSchema
import org.sollecitom.chassis.pulsar.messaging.adapter.messageProducer
import org.sollecitom.chassis.pulsar.utils.brokerURI
import java.net.URI


private val schema = CommandWasReceived.jsonSerde.asPulsarSchema()

fun commandPublisher(configuration: ResultAwareCommandPublisher.Configuration): ResultAwareCommandPublisher = PulsarNatsCommandPublisher(configuration.pulsarBrokerURI, configuration.topic, configuration.instanceInfo, schema)

interface ResultAwareCommandPublisher : ReceivedCommandPublisher<Command<*, *>, Access>, CommandResultSubscriber, Startable, Stoppable {

    data class Configuration(val pulsarBrokerURI: URI, val topic: Topic, val instanceInfo: InstanceInfo) {

        companion object
    }

    companion object
}

// TODO make this re-usable, and move it
internal class PulsarNatsCommandPublisher(private val brokerURI: URI, private val topic: Topic, private val instanceInfo: InstanceInfo, private val schema: Schema<CommandWasReceived<*>>, private val scope: CoroutineScope = CoroutineScope(SupervisorJob()), private val customize: ClientBuilder.() -> ClientBuilder = { this }) : ResultAwareCommandPublisher {

    private val pulsarClient: PulsarClient by lazy { PulsarClient.builder().customize().brokerURI(brokerURI).build() }

    // TODO use a map of producers, one per tenant
    private val messageProducer by lazy { pulsarClient.messageProducer(topic, schema, instanceInfo) }

    override suspend fun start() {

        messageProducer.start()
    }

    override suspend fun stop() {

        messageProducer.stop()
        pulsarClient.close()
    }

    context(InvocationContext<Access>)
    override suspend fun publish(event: CommandWasReceived<Command<*, *>>) {

        val message = event.asOutboundMessage()
        val messageId = messageProducer.produce(message) // TODO look up producer by tenant `this@InvocationContext.access.authenticatedOrNull()?.actor?.account?.tenant`
        logger.log { "Produced received command with type ${event.command.type} with message ID $messageId" }
    }

    context(InvocationContext<ACCESS>)
    @Suppress("UNCHECKED_CAST")
    override fun <COMMAND : Command<RESULT, ACCESS>, RESULT : Any, ACCESS : Access> resultForCommand(event: CommandWasReceived<COMMAND>): Deferred<RESULT> {

        val deferred = CompletableDeferred<RESULT>()
        //  TODO connect to NATS
        scope.launch {
            // TODO receive from NATS, parse the result, and complete the deferred
            delay(3000)
            val result = RegisterUser.Result.Accepted(user = UserWithPendingRegistration(StringId("something")))
            deferred.complete(result as RESULT)
        }
        return deferred
    }

    private fun <COMMAND : Command<*, *>> CommandWasReceived<COMMAND>.asOutboundMessage(): Message<CommandWasReceived<COMMAND>> {

        // TODO use the email address as key, and populate the properties - use a MessageConverter for this
        return OutboundMessage(key = "", value = this, properties = emptyMap(), context = Message.Context())
    }

    companion object : Loggable()
}
