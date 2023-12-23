package org.sollecitom.chassis.pulsar.messaging.event.framework.materialised.view

import kotlinx.coroutines.CoroutineScope
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.store.EventStore
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
    private val streamName = "test-pulsar-event-stream".let(::Name)
    private val instanceId = StringId("1")
    private val eventSerde: JsonSerde.SchemaAware<Event> = Event.testStubJsonSerde
    private val instances = mutableListOf<MaterialisedEventFramework>()

    context(CoroutineScope)
    override fun candidate(): MaterialisedEventFramework {

        val topic = Topic.create()
        val store = InMemoryEventStore()
        val framework = pulsar.client().pulsarMaterialisedEventFramework(topic, store, streamName, instanceId, eventSerde) { event ->

            OutboundMessage(event.id.stringValue, event, emptyMap(), Message.Context()) // TODO take from a MessageConverter
        }
        pulsarAdmin.ensureTopicExists(topic = topic, numberOfPartitions = 1, isAllowAutoUpdateSchema = true)
        framework.startBlocking()
        instances += framework
        return framework
    }

    fun PulsarClient.pulsarMaterialisedEventFramework(topic: Topic, store: EventStore.Mutable, streamName: Name, instanceId: Id, serde: JsonSerde.SchemaAware<Event>, eventToMessage: (Event) -> Message<Event>): MaterialisedEventFramework = pulsarMaterialisedEventFramework(instanceId.stringValue, streamName.value, serde, topic, store, eventToMessage)

    // TODO move to main source
    fun PulsarClient.pulsarMaterialisedEventFramework(instanceId: String, streamName: String, serde: JsonSerde.SchemaAware<Event>, topic: Topic, store: EventStore.Mutable, eventToMessage: (Event) -> Message<Event>): MaterialisedEventFramework {

        val schema = serde.asPulsarSchema()
        val producer = messageProducer(instanceId, streamName, schema, topic)
        val consumer = messageConsumer(instanceId, streamName, schema, topic)
        return MaterialisedEventFramework(topic, store, producer, consumer, eventToMessage)
    }

    private fun PulsarClient.messageProducer(instanceId: String, streamName: String, schema: Schema<Event>, topic: Topic) = pulsarMessageProducer(topic) { newProducer(schema).topic(it).producerName("$streamName-producer-${instanceId}").create() }

    private fun PulsarClient.messageConsumer(instanceId: String, streamName: String, schema: Schema<Event>, topic: Topic) = pulsarMessageConsumer(topic) { newConsumer(schema).topics(it).consumerName("$streamName-consumer-${instanceId}").subscriptionName(streamName).subscribe() }

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