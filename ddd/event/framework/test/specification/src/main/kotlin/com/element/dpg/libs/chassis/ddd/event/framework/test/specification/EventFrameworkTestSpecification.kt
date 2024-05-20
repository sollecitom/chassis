package com.element.dpg.libs.chassis.ddd.event.framework.test.specification

import assertk.assertThat
import com.element.dpg.libs.chassis.ddd.domain.framework.EventFramework
import com.element.dpg.libs.chassis.ddd.event.store.test.specification.EventStoreTestSpecification
import com.element.dpg.libs.chassis.ddd.test.stubs.testEvents
import com.element.dpg.libs.chassis.test.utils.assertions.containsSameElementsAs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

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