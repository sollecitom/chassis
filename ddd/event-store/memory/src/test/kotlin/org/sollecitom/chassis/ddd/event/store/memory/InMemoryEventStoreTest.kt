package org.sollecitom.chassis.ddd.event.store.memory

import assertk.assertThat
import kotlinx.coroutines.CoroutineStart.UNDISPATCHED
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.EntityEvent
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore
import org.sollecitom.chassis.test.utils.assertions.containsSameElementsAs
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
private class InMemoryEventStoreTest : EventStoreTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    override fun eventStore() = InMemoryEventStore()
}

// TODO move
interface EventStoreTestSpecification : CoreDataGenerator {

    val timeout: Duration get() = 10.seconds
    fun eventStore(): EventStore.Mutable

    @Test
    fun `subscribing to the stream of events`() = runTest(timeout = timeout) {

        val events = eventStore()
        val beforeSubscribingEvent = testEvent()
        events.publish(beforeSubscribingEvent)

        val receivedEvents = mutableListOf<Event>()
        val publishedEventsCount = 10
        val receivingEvents = async(start = UNDISPATCHED) { events.stream.onEach { receivedEvents += it }.take(publishedEventsCount).collect() }
        val afterSubscribingEvents = testEvents().take(publishedEventsCount).toList()
        afterSubscribingEvents.forEach { events.publish(it) }
        receivingEvents.join()

        assertThat(receivedEvents).containsSameElementsAs(afterSubscribingEvents)
    }

    @Test
    fun `consuming the history`() = runTest(timeout = timeout) {

        val events = eventStore()
        val publishedEvents = testEvents().take(15).toList()
        publishedEvents.forEach { events.publish(it) }

        val historicalEvents = events.history.all<TestEvent>().toList()

        assertThat(historicalEvents).containsSameElementsAs(publishedEvents)
    }

    @Test
    fun `subscribing to the stream of events for a given entity`() = runTest(timeout = timeout) {

        val entityId = newId.internal()
        val events = eventStore()
        val entityEvents = events.forEntity(entityId)
        val beforeSubscribingEvent = testEntityEvent(entityId = entityId)
        entityEvents.publish(beforeSubscribingEvent)

        val receivedEvents = mutableListOf<Event>()
        val publishedEventsCount = 10
        val receivingEvents = async(start = UNDISPATCHED) { entityEvents.stream.onEach { receivedEvents += it }.take(publishedEventsCount).collect() }
        val afterSubscribingEvents = testEntityEvents(entityId = entityId).take(publishedEventsCount).toList()
        val notForTheEntityEvent = testEntityEvent(entityId = newId.internal())
        events.publish(notForTheEntityEvent)
        afterSubscribingEvents.filterIndexed { index, _ -> index in 0..4 }.forEach { entityEvents.publish(it) }
        afterSubscribingEvents.filterIndexed { index, _ -> index in 5..9 }.forEach { events.publish(it) }
        receivingEvents.join()

        assertThat(receivedEvents).containsSameElementsAs(afterSubscribingEvents)
    }

    private fun testEvents(): Sequence<TestEvent> = sequence {
        while (true) {
            yield(testEvent())
        }
    }

    private fun testEntityEvents(entityId: Id? = null): Sequence<TestEntityEvent> = sequence {
        while (true) {
            yield(testEntityEvent(entityId = entityId ?: newId.internal()))
        }
    }

    private fun testEvent(id: Id = newId.internal(), timestamp: Instant = clock.now()) = TestEvent(id, timestamp)

    private fun testEntityEvent(entityId: Id = newId.internal(), id: Id = newId.internal(), timestamp: Instant = clock.now()) = TestEntityEvent(entityId, id, timestamp)

    private data class TestEvent(override val id: Id, override val timestamp: Instant) : Event {

        override val type: Event.Type get() = Type

        object Type : Event.Type {
            override val name = "test-event".let(::Name)
            override val version = 1.let(::IntVersion)
        }
    }

    private data class TestEntityEvent(override val entityId: Id, override val id: Id, override val timestamp: Instant) : EntityEvent {

        override val entityType = "test-entity".let(::Name)

        override val type: EntityEvent.Type get() = Type

        object Type : EntityEvent.Type {
            override val name = "test-event".let(::Name)
            override val version = 1.let(::IntVersion)
        }
    }
}