package org.sollecitom.chassis.correlation.core.test.utils.context

import assertk.assertThat
import assertk.assertions.hasSameSizeAs
import assertk.assertions.isEqualTo
import assertk.assertions.isLessThanOrEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.networking.IpAddress
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.origin.Origin
import org.sollecitom.chassis.correlation.core.domain.trace.ExternalInvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.Trace
import org.sollecitom.chassis.correlation.core.test.utils.origin.create
import org.sollecitom.chassis.correlation.core.test.utils.trace.create

@TestInstance(PER_CLASS)
private class InvocationContextTestFactoryExampleTests : WithCoreGenerators by WithCoreGenerators.testProvider {

    @Test
    fun `customizing the trace`() {

        val actionId = newId.string()
        val trace = Trace.create(externalInvocationTrace = ExternalInvocationTrace.create(actionId = actionId))

        val context = InvocationContext.create(testTrace = { trace })

        assertThat(context.trace).isEqualTo(trace)
    }

    @Test
    fun `customizing the origin`() {

        val origin = Origin.create(ipAddress = IpAddress.create("152.38.16.4"))

        val context = InvocationContext.create(testOrigin = { origin })

        assertThat(context.origin).isEqualTo(origin)
    }

    @Test
    fun `bucket sort example`() {

        val possibleElements = (4..15).toList()
        val values = (1..100).map { possibleElements.random() }

        val sorted = values.bucketSort(possibleElements)

        println(values)
        println(sorted)
        assertThat(sorted).hasSameSizeAs(values)
        sorted.zipWithNext().forEach { (current, next) ->
            assertThat(current, "$current").isLessThanOrEqualTo(next)
        }
    }

    fun <V> Iterable<V>.bucketSort(orderedPossibleElements: List<V>): List<V> {

        val buckets = IntArray(orderedPossibleElements.size)
        val indexForElement = orderedPossibleElements.withIndex().associate { it.value to it.index }
        forEach { value ->
            val bucketIndex = indexForElement[value]!!
            buckets[bucketIndex]++
        }
        return buckets.withIndex().flatMap { (index, count) -> List(count) { orderedPossibleElements[index] } }
    }
}