package org.sollecitom.chassis.pulsar.messaging.adapter

import kotlinx.coroutines.CoroutineScope
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.InstanceInfo
import org.sollecitom.chassis.core.domain.lifecycle.startBlocking
import org.sollecitom.chassis.core.domain.lifecycle.stopBlocking
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.event.framework.test.specification.EventFrameworkTestSpecification
import org.sollecitom.chassis.ddd.event.store.memory.InMemoryEventStore
import org.sollecitom.chassis.ddd.stubs.serialization.json.event.testStubJsonSerde
import org.sollecitom.chassis.messaging.domain.MessageStream
import org.sollecitom.chassis.messaging.domain.Topic
import org.sollecitom.chassis.messaging.domain.asEventStream
import org.sollecitom.chassis.messaging.event.framework.materialised.view.MaterialisedEventFramework
import org.sollecitom.chassis.messaging.test.utils.create
import org.sollecitom.chassis.pulsar.json.serialization.asPulsarSchema
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
        val store = InMemoryEventStore()
        val stream = MessageStream.pulsar(instanceInfo, topic, Event.testStubJsonSerde.asPulsarSchema(), pulsar::client).asEventStream { event -> event.id.stringValue }
        val framework = MaterialisedEventFramework(store, stream)
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

        instances.forEach(MaterialisedEventFramework::stopBlocking)
        pulsar.stop()
    }
}