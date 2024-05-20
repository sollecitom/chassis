package com.element.dpg.libs.chassis.correlation.core.domain.trace

import kotlinx.datetime.Instant
import kotlin.time.Duration

data class Trace(val invocation: InvocationTrace, val parent: InvocationTrace = invocation, val originating: InvocationTrace = parent, val external: ExternalInvocationTrace) {

    init {
        require(invocation != originating || invocation == parent) { "If invocation == originating, then invocation == parent must also be true" }
    }

    fun fork(invocation: InvocationTrace) = Trace(parent = this.invocation, invocation = invocation, originating = originating, external = this.external)

    val isOriginating: Boolean get() = invocation == originating
    val isParent: Boolean get() = invocation == parent

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