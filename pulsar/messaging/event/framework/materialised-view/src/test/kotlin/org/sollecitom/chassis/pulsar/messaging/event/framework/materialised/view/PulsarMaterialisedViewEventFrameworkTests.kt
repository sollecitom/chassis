package org.sollecitom.chassis.pulsar.messaging.event.framework.materialised.view

import kotlinx.coroutines.CoroutineScope
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.event.framework.test.specification.EventFrameworkTestSpecification
import org.sollecitom.chassis.ddd.event.store.memory.InMemoryEventStore
import org.sollecitom.chassis.ddd.stubs.serialization.json.event.testStubJsonSerde
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.messaging.domain.Message
import org.sollecitom.chassis.messaging.domain.OutboundMessage
import org.sollecitom.chassis.messaging.domain.Topic
import org.sollecitom.chassis.messaging.event.framework.materialised.view.MaterialisedEventFramework
import org.sollecitom.chassis.messaging.test.utils.create
import org.sollecitom.chassis.pulsar.json.serialization.asPulsarSchema
import org.sollecitom.chassis.pulsar.messaging.adapter.*
import org.sollecitom.chassis.pulsar.test.utils.admin
import org.sollecitom.chassis.pulsar.test.utils.client
import org.sollecitom.chassis.pulsar.test.utils.newPulsarContainer
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
private class PulsarMaterialisedViewEventFrameworkTests : EventFrameworkTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val timeout = 20.seconds
    private val pulsar = newPulsarContainer()
    private val pulsarAdmin by lazy { pulsar.admin() }
    private val streamName = "test-pulsar-event-stream"
    private val instanceId = StringId("1")
    private val eventSerde: JsonSerde.SchemaAware<Event> = Event.testStubJsonSerde
    private val instances = mutableListOf<MaterialisedEventFramework>()

    context(CoroutineScope)
    override fun candidate(): MaterialisedEventFramework {

        val topic = Topic.create()
        val store = InMemoryEventStore()
        val schema = eventSerde.asPulsarSchema()
        // TODO create a convenience function to create the framework in Pulsar
        val producer = pulsarMessageProducer(topic) { pulsar.client().newProducer(schema).topic(it).producerName("$streamName-producer-${instanceId.stringValue}").create() }
        val consumer = pulsarMessageConsumer(topic) { pulsar.client().newConsumer(schema).topics(it).consumerName("$streamName-consumer-${instanceId.stringValue}").subscriptionName(streamName).subscribe() }
        val framework = MaterialisedEventFramework(topic, store, producer, consumer) { event ->

            OutboundMessage(event.id.stringValue, event, emptyMap(), Message.Context()) // TODO take from a MessageConverter
        }
        pulsarAdmin.ensureTopicExists(topic = topic, numberOfPartitions = 1, isAllowAutoUpdateSchema = true)
        framework.startBlocking()
        instances += framework
        return framework
    }

    @BeforeAll
    fun beforeAll() {

        pulsar.start()
    }

    @AfterAll
    fun afterAll() {

        instances.forEach { it.stopBlocking() }
        pulsar.stop()
    }
}