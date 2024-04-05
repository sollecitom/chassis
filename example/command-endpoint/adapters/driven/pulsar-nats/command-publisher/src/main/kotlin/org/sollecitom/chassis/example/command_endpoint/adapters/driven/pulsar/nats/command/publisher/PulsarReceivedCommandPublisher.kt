package org.sollecitom.chassis.example.command_endpoint.adapters.driven.pulsar.nats.command.publisher

import org.apache.pulsar.client.api.ClientBuilder
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema
import org.sollecitom.chassis.core.domain.identity.InstanceInfo
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.context.targetTenant
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.logging.utils.log
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.ddd.domain.CommandWasReceived
import org.sollecitom.chassis.ddd.domain.ReceivedCommandPublisher
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.messaging.domain.TenantAgnosticTopic
import org.sollecitom.chassis.pulsar.messaging.adapter.messageProducer
import org.sollecitom.chassis.pulsar.utils.brokerURI
import java.net.URI

// TODO move it somewhere shared
internal class PulsarReceivedCommandPublisher(private val brokerURI: URI, private val topic: TenantAgnosticTopic, private val instanceInfo: InstanceInfo, private val schema: Schema<CommandWasReceived<*>>, private val messageConverter: MessageConverter<CommandWasReceived<*>>, private val customize: ClientBuilder.() -> ClientBuilder = { this }) : ReceivedCommandPublisher<Command<*, *>, Access> {

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
        val topicWithTenant = topic.withTenant(targetTenant)
        val messageId = messageProducer.produce(message, topicWithTenant)
        logger.log { "Produced received command with type ${event.command.type} with message ID $messageId" }
    }

    private fun TenantAgnosticTopic.withTenant(tenant: Tenant) = withTenant(tenant.id.stringValue.let(::Name))

    companion object : Loggable()
}