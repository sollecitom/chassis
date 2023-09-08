package org.sollecitom.chassis.ddd.event.store.test.specification

import assertk.assertThat
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore
import org.sollecitom.chassis.test.utils.assertions.containsSameElementsAs
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

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
        val receivingEvents = async(start = CoroutineStart.UNDISPATCHED) { events.stream.onEach { receivedEvents += it }.take(publishedEventsCount).collect() }
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
        val receivingEvents = async(start = CoroutineStart.UNDISPATCHED) { entityEvents.stream.onEach { receivedEvents += it }.take(publishedEventsCount).collect() }
        val afterSubscribingEvents = testEntityEvents(entityId = entityId).take(publishedEventsCount).toList()
        val notAnEntityEvent = testEvent()
        val notForTheEntityEvent = testEntityEvent(entityId = newId.internal())
        events.publish(notAnEntityEvent)
        events.publish(notForTheEntityEvent)
        afterSubscribingEvents.filterIndexed { index, _ -> index in 0..4 }.forEach { entityEvents.publish(it) }
        afterSubscribingEvents.filterIndexed { index, _ -> index in 5..9 }.forEach { events.publish(it) }
        receivingEvents.join()

        assertThat(receivedEvents).containsSameElementsAs(afterSubscribingEvents)
    }

    @Test
    fun `consuming the history for a given entity`() = runTest(timeout = timeout) {

        val entityId = newId.internal()
        val events = eventStore()
        val entityEvents = events.forEntity(entityId)
        val notAnEntityEvent = testEvent()
        val notForTheEntityEvent = testEntityEvent(entityId = newId.internal())
        events.publish(notAnEntityEvent)
        events.publish(notForTheEntityEvent)
        val publishedEvents = testEntityEvents(entityId = entityId).take(12).toList()
        publishedEvents.filterIndexed { index, _ -> index in 0..5 }.forEach { entityEvents.publish(it) }
        publishedEvents.filterIndexed { index, _ -> index in 6..11 }.forEach { events.publish(it) }

        val historicalEvents = entityEvents.history.all<TestEvent>().toList()

        assertThat(historicalEvents).containsSameElementsAs(publishedEvents)
    }
}