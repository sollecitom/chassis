package org.sollecitom.chassis.example.command_endpoint.adapters.driven.pulsar.nats.command.publisher

import kotlinx.coroutines.*
import org.sollecitom.chassis.ddd.domain.CommandWasReceived
import org.sollecitom.chassis.example.event.serialization.json.jsonSerde
import org.sollecitom.chassis.pulsar.json.serialization.asPulsarSchema

private val schema = CommandWasReceived.jsonSerde.asPulsarSchema()

fun pulsarAndNatsCommandPublisher(configuration: PulsarAndNatsCommandPublisherConfiguration, messageConverter: MessageConverter<CommandWasReceived<*>> = CommandWasReceivedMessageConverter, scope: CoroutineScope = CoroutineScope(SupervisorJob())): ResultAwareCommandPublisher = CompositeResultAwareCommandPublisher(PulsarReceivedCommandPublisher(configuration.pulsarBrokerURI, configuration.topic, configuration.instanceInfo, schema, messageConverter), NatsCommandResultSubscriber(scope))