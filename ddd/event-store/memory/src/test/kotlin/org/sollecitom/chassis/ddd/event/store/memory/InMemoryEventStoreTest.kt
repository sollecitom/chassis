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
        val afterSubscribingEvents = testEvents.take(publishedEventsCount).toList()
        afterSubscribingEvents.forEach { events.publish(it) }
        receivingEvents.join()

        assertThat(receivedEvents).containsSameElementsAs(afterSubscribingEvents)
    }

    @Test
    fun `consuming the history`() = runTest(timeout = timeout) {

        val events = eventStore()
        val publishedEvents = testEvents.take(15).toList()
        publishedEvents.forEach { events.publish(it) }

        val historicalEvents = events.history.all<TestEvent>().toList()

        assertThat(historicalEvents).containsSameElementsAs(publishedEvents)
    }

    private val testEvents: Sequence<TestEvent>
        get() = sequence {
            while (true) {
                yield(testEvent())
            }
        }

    private fun testEvent(id: Id = newId.internal(), timestamp: Instant = clock.now()) = TestEvent(id, timestamp)

    private data class TestEvent(override val id: Id, override val timestamp: Instant) : Event {

        override val type: Event.Type get() = Type

        object Type : Event.Type {
            override val name = "test-event".let(::Name)
            override val version = 1.let(::IntVersion)
        }
    }
}