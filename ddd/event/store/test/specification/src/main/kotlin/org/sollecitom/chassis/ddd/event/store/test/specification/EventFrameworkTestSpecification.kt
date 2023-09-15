package org.sollecitom.chassis.ddd.event.store.test.specification

import assertk.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.ddd.domain.store.EventFramework
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
        publishedEvents.map { events.publish(it) }.joinAll()

        val historicalEvents = events.all().toList()

        assertThat(historicalEvents).containsSameElementsAs(publishedEvents)
    }
}