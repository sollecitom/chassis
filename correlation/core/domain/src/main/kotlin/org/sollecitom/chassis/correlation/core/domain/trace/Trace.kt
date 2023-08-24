package org.sollecitom.chassis.correlation.core.domain.trace

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import kotlin.time.Duration

data class Trace<ID : SortableTimestampedUniqueIdentifier<ID>>(val invocation: InvocationTrace<ID>, val parent: InvocationTrace<ID> = invocation, val originating: InvocationTrace<ID> = parent, val external: ExternalInvocationTrace) {

    fun fork(invocation: InvocationTrace<ID>) = Trace(parent = this.invocation, invocation = invocation, external = this.external)

    val elapsedTime: ElapsedTimeSelector = ElapsedTimeSelectorAdapter()

    interface ElapsedTimeSelector {

        fun sinceInvocationStarted(timeNow: Instant): Duration

        fun sinceParentInvocationStarted(timeNow: Instant): Duration

        fun sinceOriginatingInvocationStarted(timeNow: Instant): Duration
    }

    private inner class ElapsedTimeSelectorAdapter : ElapsedTimeSelector {

        override fun sinceInvocationStarted(timeNow: Instant) = timeNow - invocation.createdAt

        override fun sinceParentInvocationStarted(timeNow: Instant) = timeNow - parent.createdAt

        override fun sinceOriginatingInvocationStarted(timeNow: Instant) = timeNow - originating.createdAt
    }

    companion object
}