package org.sollecitom.chassis.pulsar.messaging.adapter

import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.api.SubscriptionInitialPosition
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.logging.standard.configuration.configureLogging
import org.sollecitom.chassis.messaging.domain.MessageConsumer
import org.sollecitom.chassis.messaging.domain.MessageProducer
import org.sollecitom.chassis.messaging.domain.Topic
import org.sollecitom.chassis.messaging.test.utils.MessagingTestSpecification
import org.sollecitom.chassis.messaging.test.utils.create
import org.sollecitom.chassis.pulsar.test.utils.admin
import org.sollecitom.chassis.pulsar.test.utils.client
import org.sollecitom.chassis.pulsar.test.utils.newPulsarContainer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
private class PulsarMessagingTests : MessagingTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    init {
        configureLogging(defaultMinimumLoggingLevel = LoggingLevel.INFO)
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