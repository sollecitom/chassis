package org.sollecitom.chassis.example.command_endpoint.adapters.driven.pulsar.nats.command.publisher

import org.sollecitom.chassis.core.domain.identity.InstanceInfo
import org.sollecitom.chassis.messaging.domain.TenantAgnosticTopic
import java.net.URI

data class PulsarAndNatsCommandPublisherConfiguration(val pulsarBrokerURI: URI, val topic: TenantAgnosticTopic, val instanceInfo: InstanceInfo) {

    companion object
}