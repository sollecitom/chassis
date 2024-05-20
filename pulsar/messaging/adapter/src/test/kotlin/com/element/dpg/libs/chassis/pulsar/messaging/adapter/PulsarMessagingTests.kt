package com.element.dpg.libs.chassis.pulsar.messaging.adapter

import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.logging.standard.configuration.configureLogging
import com.element.dpg.libs.chassis.messaging.domain.MessageConsumer
import com.element.dpg.libs.chassis.messaging.domain.MessageProducer
import com.element.dpg.libs.chassis.messaging.domain.Topic
import com.element.dpg.libs.chassis.messaging.test.utils.MessagingTestSpecification
import com.element.dpg.libs.chassis.messaging.test.utils.create
import com.element.dpg.libs.chassis.pulsar.test.utils.admin
import com.element.dpg.libs.chassis.pulsar.test.utils.client
import com.element.dpg.libs.chassis.pulsar.test.utils.newPulsarContainer
import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.api.SubscriptionInitialPosition
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
private class PulsarMessagingTests : MessagingTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    init {
        configureLogging(defaultMinimumLoggingLevel = com.element.dpg.libs.chassis.logger.core.LoggingLevel.INFO)
    }

    private val pulsar = newPulsarContainer()
    private val pulsarClient by lazy { pulsar.client() }
    private val pulsarAdmin by lazy { pulsar.admin() }
    override val timeout: Duration get() = 30.seconds

    @BeforeAll
    fun beforeAll() = pulsar.start()

    @AfterAll
    fun afterAll() = pulsar.stop()

    override fun newTopic(): Topic = Topic.create().also { pulsarAdmin.ensureTopicExists(topic = it, isAllowAutoUpdateSchema = true) }

    override fun newMessageProducer(topic: Topic, name: String): MessageProducer<String> = pulsarMessageProducer(name.let(::Name)) { pulsarClient.newProducer(Schema.STRING).topic(it) }

    override fun newMessageConsumer(topics: Set<Topic>, subscriptionName: String, name: String): MessageConsumer<String> = pulsarMessageConsumer(topics) { pulsarClient.newConsumer(Schema.STRING).topics(it).subscriptionName(subscriptionName).subscriptionInitialPosition(SubscriptionInitialPosition.Earliest).consumerName(name).subscribe() }
}