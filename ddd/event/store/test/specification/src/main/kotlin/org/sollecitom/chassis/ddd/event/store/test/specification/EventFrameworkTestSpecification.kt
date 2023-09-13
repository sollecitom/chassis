package org.sollecitom.chassis.ddd.event.store.test.specification

import assertk.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.ddd.domain.store.EventFramework
import org.sollecitom.chassis.ddd.test.stubs.TestEvent
import org.sollecitom.chassis.ddd.test.stubs.testEvents
import org.sollecitom.chassis.test.utils.assertions.containsSameElementsAs

// TODO move to another module
interface EventFrameworkTestSpecification : EventStoreTestSpecification {

    context(CoroutineScope)
    override fun candidate(): EventFramework.Mutable

    @Test
    fun `publishing events`() = runTest(timeout = timeout) {

        val events = candidate()
        val publishedEvents = testEvents().take(15).toList()
        publishedEvents.forEach { events.publish(it) }

        testScheduler.advanceUntilIdle()
        val historicalEvents = events.all<TestEvent>().toList()

        assertThat(historicalEvents).containsSameElementsAs(publishedEvents)
    }
}