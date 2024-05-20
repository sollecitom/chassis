@file:OptIn(ExperimentalTime::class)

package com.element.dpg.libs.chassis.ddd.event.store.test.specification

import assertk.assertThat
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.ddd.domain.store.EventStore
import com.element.dpg.libs.chassis.ddd.test.stubs.testEntityEvent
import com.element.dpg.libs.chassis.ddd.test.stubs.testEntityEvents
import com.element.dpg.libs.chassis.ddd.test.stubs.testEvent
import com.element.dpg.libs.chassis.ddd.test.stubs.testEvents
import com.element.dpg.libs.chassis.test.utils.assertions.containsSameElementsAs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

interface EventStoreTestSpecification : CoreDataGenerator {

    val timeout: Duration get() = 10.seconds

    context(CoroutineScope)
    fun candidate(): EventStore.Mutable

    @Test
    fun `consuming the history`() = runTest(timeout = timeout) {

        val events = candidate()
        val publishedEvents = testEvents().take(15).toList()
        publishedEvents.forEach { events.store(it) }

        val historicalEvents = events.all().toList()

        assertThat(historicalEvents).containsSameElementsAs(publishedEvents)
    }

    @Test
    fun `consuming the history for a given entity`() = runTest(timeout = timeout) {

        val entityId = newId.internal()
        val events = candidate()
        val entityEvents = events.forEntityId(entityId)
        val notAnEntityEvent = testEvent()
        val notForTheEntityEvent = testEntityEvent(entityId = newId.internal())
        events.store(notAnEntityEvent)
        events.store(notForTheEntityEvent)
        val publishedEvents = testEntityEvents(entityId = entityId).take(12).toList()
        publishedEvents.filterIndexed { index, _ -> index in 0..5 }.forEach { entityEvents.store(it) }
        publishedEvents.filterIndexed { index, _ -> index in 6..11 }.forEach { events.store(it) }

        val historicalEvents = entityEvents.all().toList()

        assertThat(historicalEvents).containsSameElementsAs(publishedEvents)
    }
}