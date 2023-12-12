package org.sollecitom.chassis.pulsar.messaging.adapter

import assertk.assertThat
import assertk.assertions.isNotNull
import org.apache.pulsar.client.api.Schema
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.logging.standard.configuration.configureLogging
import org.sollecitom.chassis.pulsar.test.utils.admin
import org.sollecitom.chassis.pulsar.test.utils.client
import org.sollecitom.chassis.pulsar.test.utils.create
import org.sollecitom.chassis.pulsar.test.utils.newPulsarContainer
import org.sollecitom.chassis.pulsar.utils.PulsarTopic
import org.sollecitom.chassis.pulsar.utils.ensureTopicExists
import org.sollecitom.chassis.pulsar.utils.topic
import org.sollecitom.chassis.pulsar.utils.topics
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
private class PulsarMessagingTests : CoreDataGenerator by CoreDataGenerator.testProvider { // TODO turn into a spec re-usable across Pulsar and Kafka

    init {
        configureLogging(defaultMinimumLoggingLevel = LoggingLevel.INFO)
    }

    private val pulsar = newPulsarContainer()
    private val pulsarClient by lazy { pulsar.client() }
    private val pulsarAdmin by lazy { pulsar.admin() }
    private val topic = PulsarTopic.create() // TODO change this to be a messaging topic instead
    private val timeout: Duration get() = 30.seconds

    @BeforeAll
    fun beforeAll() {
        pulsar.start()
        pulsarAdmin.ensureTopicExists(topic = topic, isAllowAutoUpdateSchema = true)
    }

    @AfterAll
    fun afterAll() {
        pulsar.stop()
    }

    @Test
    fun `sending and receiving a single message`() {

        val consumer = pulsarClient.newConsumer(Schema.STRING).topics(topic).subscriptionName("a subscription").subscribe()
        val producer = pulsarClient.newProducer(Schema.STRING).topic(topic).producerName("a producer").create()

        assertThat(pulsar.pulsarBrokerUrl).isNotNull()
    }
}