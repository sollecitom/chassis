package org.sollecitom.chassis.pulsar.messaging.event.framework.materialised.view

import kotlinx.coroutines.CoroutineScope
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.InstanceInfo
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.ddd.event.framework.test.specification.EventFrameworkTestSpecification
import org.sollecitom.chassis.ddd.event.store.memory.InMemoryEventStore
import org.sollecitom.chassis.ddd.stubs.serialization.json.event.testStubJsonSerde
import org.sollecitom.chassis.ddd.test.stubs.TestEntityEvent
import org.sollecitom.chassis.ddd.test.stubs.TestEvent
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
    private val instanceInfo = InstanceInfo(id = 1, groupName = "test-pulsar-event-stream".let(::Name), maximumInstancesCount = 256)
    private val instances = mutableListOf<MaterialisedEventFramework>()

    context(CoroutineScope)
    override fun candidate(): MaterialisedEventFramework {

        val topic = Topic.create()
        val stream = TestJsonPulsarEventStream(topic)
        val store = InMemoryEventStore()
        val framework = pulsar.client().pulsarMaterialisedEventFramework(instanceInfo, stream, store)
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

// TODO move to main source
fun PulsarClient.pulsarMaterialisedEventFramework(instanceInfo: InstanceInfo, stream: PulsarEventStream, store: EventStore.Mutable): MaterialisedEventFramework {

    val producer = messageProducer(instanceInfo, stream)
    val consumer = messageConsumer(instanceInfo, stream)
    return MaterialisedEventFramework(stream.topic, store, producer, consumer) { event ->

        OutboundMessage(stream.messageKeyForEvent(event), event, stream.messagePropertiesForEvent(event), Message.Context())
    }
}

private fun PulsarClient.messageProducer(instanceInfo: InstanceInfo, stream: PulsarEventStream) = pulsarMessageProducer(stream.topic) { newProducer(stream.schema).topic(it).producerName("${instanceInfo.groupName.value}-producer-${instanceInfo.id}").create() }

private fun PulsarClient.messageConsumer(instanceInfo: InstanceInfo, stream: PulsarEventStream) = pulsarMessageConsumer(stream.topic) { newConsumer(stream.schema).topics(it).consumerName("${instanceInfo.groupName.value}-consumer-${instanceInfo.id}").subscriptionName(instanceInfo.groupName.value).subscribe() }

// TODO move
interface PulsarEventStream : EventStream {

    // TODO consider making it generic to the base event type e.g., GenericTestEvent in the stub
    val schema: Schema<Event>
}

private class TestJsonPulsarEventStream(topic: Topic) : PulsarEventStream, EventStream by TestEventStream(topic) {

    override val schema = Event.testStubJsonSerde.asPulsarSchema()
}

private data class TestEventStream(override val topic: Topic) : EventStream {

    override fun messageKeyForEvent(event: Event) = when (event) {
        is TestEvent -> event.id.stringValue
        is TestEntityEvent -> event.entityId.stringValue
        else -> error("Unsupported event $event")
    }

    override fun messagePropertiesForEvent(event: Event) = emptyMap<String, String>()
}

// TODO move
interface EventStream {

    val topic: Topic

    fun messageKeyForEvent(event: Event): String

    fun messagePropertiesForEvent(event: Event): Map<String, String>
}