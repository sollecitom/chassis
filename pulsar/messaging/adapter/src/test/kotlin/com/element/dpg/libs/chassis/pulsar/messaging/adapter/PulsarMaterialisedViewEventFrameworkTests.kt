package com.element.dpg.libs.chassis.pulsar.messaging.adapter

import kotlinx.coroutines.CoroutineScope
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import com.element.dpg.libs.chassis.core.domain.identity.InstanceInfo
import com.element.dpg.libs.chassis.core.domain.lifecycle.startBlocking
import com.element.dpg.libs.chassis.core.domain.lifecycle.stopBlocking
import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.ddd.domain.Event
import com.element.dpg.libs.chassis.ddd.event.framework.test.specification.EventFrameworkTestSpecification
import com.element.dpg.libs.chassis.ddd.event.store.memory.InMemoryEventStore
import com.element.dpg.libs.chassis.ddd.stubs.serialization.json.event.testStubJsonSerde
import com.element.dpg.libs.chassis.messaging.domain.MessageStream
import com.element.dpg.libs.chassis.messaging.domain.Topic
import com.element.dpg.libs.chassis.messaging.domain.asEventStream
import com.element.dpg.libs.chassis.messaging.event.framework.materialised.view.MaterialisedEventFramework
import com.element.dpg.libs.chassis.messaging.test.utils.create
import com.element.dpg.libs.chassis.pulsar.json.serialization.asPulsarSchema
import com.element.dpg.libs.chassis.pulsar.test.utils.admin
import com.element.dpg.libs.chassis.pulsar.test.utils.client
import com.element.dpg.libs.chassis.pulsar.test.utils.newPulsarContainer
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