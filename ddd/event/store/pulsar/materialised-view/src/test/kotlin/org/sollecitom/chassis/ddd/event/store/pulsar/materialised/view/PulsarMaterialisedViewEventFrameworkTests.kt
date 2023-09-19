package org.sollecitom.chassis.ddd.event.store.pulsar.materialised.view

import kotlinx.coroutines.CoroutineScope
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.event.store.memory.InMemoryEventStore
import org.sollecitom.chassis.ddd.event.store.test.specification.EventFrameworkTestSpecification
import org.sollecitom.chassis.ddd.stubs.serialization.json.event.testStubJsonSerde
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.pulsar.json.serialization.pulsarAvroSchema
import org.sollecitom.chassis.pulsar.test.utils.admin
import org.sollecitom.chassis.pulsar.test.utils.client
import org.sollecitom.chassis.pulsar.test.utils.create
import org.sollecitom.chassis.pulsar.test.utils.newPulsarContainer
import org.sollecitom.chassis.pulsar.utils.PulsarTopic
import org.sollecitom.chassis.pulsar.utils.ensureTopicExists
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
private class PulsarMaterialisedViewEventFrameworkTests : EventFrameworkTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val timeout = 20.seconds
    private val pulsar = newPulsarContainer()
    private val pulsarAdmin by lazy { pulsar.admin() }
    private val streamName = "test-pulsar-event-stream".let(::Name)
    private val instanceId = StringId("1")
    private val eventSerde: JsonSerde.SchemaAware<Event> = Event.testStubJsonSerde
    private val instances = mutableListOf<PulsarEventFramework>()
    context(CoroutineScope)
    override fun candidate() = createEventStore()

    context(CoroutineScope)
    private fun createEventStore(): PulsarEventFramework {

        val topic = PulsarTopic.create()
        val framework = PulsarEventFramework(topic, streamName, instanceId, eventSerde.pulsarAvroSchema(), pulsar.pulsarBrokerUrl, InMemoryEventStore())
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