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
import org.sollecitom.chassis.ddd.domain.Events
import org.sollecitom.chassis.test.utils.assertions.containsSameElementsAs
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface EventStoreTestSpecification : CoreDataGenerator {

    val timeout: Duration get() = 10.seconds
    fun events(): Events.Mutable

    @Test
    fun `subscribing to the stream of events`() = runTest(timeout = timeout) {

        val events = events()
        val beforeSubscribingEvent = testEvent()
        events.stream.publish(beforeSubscribingEvent)

        val receivedEvents = mutableListOf<Event>()
        val publishedEventsCount = 10
        val receivingEvents = async(start = CoroutineStart.UNDISPATCHED) { events.stream.asFlow.onEach { receivedEvents += it }.take(publishedEventsCount).collect() }
        val afterSubscribingEvents = testEvents().take(publishedEventsCount).toList()
        afterSubscribingEvents.forEach { events.stream.publish(it) }
        receivingEvents.join()

        assertThat(receivedEvents).containsSameElementsAs(afterSubscribingEvents)
    }

    @Test
    fun `consuming the history`() = runTest(timeout = timeout) {

        val events = events()
        val publishedEvents = testEvents().take(15).toList()
        publishedEvents.forEach { events.stream.publish(it) }

        val historicalEvents = events.store.all<TestEvent>().toList()

        assertThat(historicalEvents).containsSameElementsAs(publishedEvents)
    }

    @Test
    fun `subscribing to the stream of events for a given entity`() = runTest(timeout = timeout) {

        val entityId = newId.internal()
        val events = events()
        val entityEvents = events.forEntityId(entityId)
        val beforeSubscribingEvent = testEntityEvent(entityId = entityId)
        entityEvents.stream.publish(beforeSubscribingEvent)

        val receivedEvents = mutableListOf<Event>()
        val publishedEventsCount = 10
        val receivingEvents = async(start = CoroutineStart.UNDISPATCHED) { entityEvents.stream.asFlow.onEach { receivedEvents += it }.take(publishedEventsCount).collect() }
        val afterSubscribingEvents = testEntityEvents(entityId = entityId).take(publishedEventsCount).toList()
        val notAnEntityEvent = testEvent()
        val notForTheEntityEvent = testEntityEvent(entityId = newId.internal())
        events.stream.publish(notAnEntityEvent)
        events.stream.publish(notForTheEntityEvent)
        afterSubscribingEvents.filterIndexed { index, _ -> index in 0..4 }.forEach { entityEvents.stream.publish(it) }
        afterSubscribingEvents.filterIndexed { index, _ -> index in 5..9 }.forEach { events.stream.publish(it) }
        receivingEvents.join()

        assertThat(receivedEvents).containsSameElementsAs(afterSubscribingEvents)
    }

    @Test
    fun `consuming the history for a given entity`() = runTest(timeout = timeout) {

        val entityId = newId.internal()
        val events = events()
        val entityEvents = events.forEntityId(entityId)
        val notAnEntityEvent = testEvent()
        val notForTheEntityEvent = testEntityEvent(entityId = newId.internal())
        events.stream.publish(notAnEntityEvent)
        events.stream.publish(notForTheEntityEvent)
        val publishedEvents = testEntityEvents(entityId = entityId).take(12).toList()
        publishedEvents.filterIndexed { index, _ -> index in 0..5 }.forEach { entityEvents.stream.publish(it) }
        publishedEvents.filterIndexed { index, _ -> index in 6..11 }.forEach { events.stream.publish(it) }

        val historicalEvents = entityEvents.store.all<TestEntityEvent>().toList()

        assertThat(historicalEvents).containsSameElementsAs(publishedEvents)
    }
}