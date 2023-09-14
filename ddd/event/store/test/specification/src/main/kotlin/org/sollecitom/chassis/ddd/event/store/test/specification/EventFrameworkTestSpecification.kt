package org.sollecitom.chassis.ddd.event.store.test.specification

import assertk.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.ddd.domain.store.EventFramework
import org.sollecitom.chassis.ddd.test.stubs.testEvents
import org.sollecitom.chassis.test.utils.assertions.containsSameElementsAs
import org.sollecitom.chassis.test.utils.coroutines.pauseFor
import kotlin.time.Duration.Companion.seconds

// TODO move to another module
interface EventFrameworkTestSpecification : EventStoreTestSpecification {

    context(CoroutineScope)
    override fun candidate(): EventFramework.Mutable

    @Test
    fun `publishing events`() = runTest(timeout = timeout) {

        val events = candidate()
        val publishedEvents = testEvents().take(15).toList()
        publishedEvents.forEach { events.publish(it) }
        pauseFor(1.seconds)

        val historicalEvents = events.all().toList()

        assertThat(historicalEvents).containsSameElementsAs(publishedEvents)
    }
}