package org.sollecitom.chassis.ddd.event.stream.test.specification

import assertk.assertThat
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.stream.EventStream
import org.sollecitom.chassis.ddd.test.stubs.testEntityEvent
import org.sollecitom.chassis.ddd.test.stubs.testEntityEvents
import org.sollecitom.chassis.ddd.test.stubs.testEvent
import org.sollecitom.chassis.ddd.test.stubs.testEvents
import org.sollecitom.chassis.test.utils.assertions.containsSameElementsAs
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface EventStreamTestSpecification : CoreDataGenerator {

    val timeout: Duration get() = 10.seconds
    fun eventStream(): EventStream.Mutable

    @Test
    fun `subscribing to the stream of events`() = runTest(timeout = timeout) {

        val events = eventStream()
        val beforeSubscribingEvent = testEvent()
        events.publish(beforeSubscribingEvent)

        val receivedEvents = mutableListOf<Event>()
        val publishedEventsCount = 10
        val receivingEvents = async(start = CoroutineStart.UNDISPATCHED) { events.asFlow.onEach { receivedEvents += it }.take(publishedEventsCount).collect() }
        val afterSubscribingEvents = testEvents().take(publishedEventsCount).toList()
        afterSubscribingEvents.forEach { events.publish(it) }
        receivingEvents.join()

        assertThat(receivedEvents).containsSameElementsAs(afterSubscribingEvents)
    }

    @Test
    fun `subscribing to the stream of events for a given entity`() = runTest(timeout = timeout) {

        val entityId = newId.internal()
        val events = eventStream()
        val entityEvents = events.forEntityId(entityId)
        val beforeSubscribingEvent = testEntityEvent(entityId = entityId)
        entityEvents.publish(beforeSubscribingEvent)

        val receivedEvents = mutableListOf<Event>()
        val publishedEventsCount = 10
        val receivingEvents = async(start = CoroutineStart.UNDISPATCHED) { entityEvents.asFlow.onEach { receivedEvents += it }.take(publishedEventsCount).collect() }
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
}