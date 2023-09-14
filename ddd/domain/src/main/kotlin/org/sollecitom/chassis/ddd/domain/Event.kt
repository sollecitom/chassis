package org.sollecitom.chassis.ddd.domain

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.traits.Identifiable
import org.sollecitom.chassis.core.domain.traits.Timestamped
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace

interface Event : Happening, Identifiable, Timestamped {

    val context: Context
    val reference: Reference get() = Reference(id, type, timestamp)

    fun forkContext(invocation: InvocationTrace): Context = context.fork(invocationTrace = invocation, parent = reference)

    data class Reference(override val id: Id, val type: Happening.Type, override val timestamp: Instant) : Timestamped, Identifiable {

        companion object
    }

    data class Context(val invocation: InvocationContext<Access>, val parent: Reference? = null, val originating: Reference? = null) {

        init {
            require(parent == null && originating == null || parent != null && originating != null) { "Parent and originating event references must both be either specified or omitted" }
        }

        fun fork(invocationTrace: InvocationTrace, parent: Reference?) = copy(invocation = invocation.fork(invocationTrace), parent = parent, originating = originating ?: parent)

        val isOriginating: Boolean get() = originating == null

        companion object
    }

    companion object
}

val Event.isOriginating: Boolean get() = context.isOriginating

context(InvocationContext<*>)
fun Event.forkContext(): Event.Context = forkContext(invocation = this@InvocationContext.trace.invocation)

fun InvocationContext<*>.toEventContext(parentEvent: Event.Reference? = null, originatingEvent: Event.Reference? = null): Event.Context = Event.Context(this, parentEvent, originatingEvent)

fun Event.descendsFromEventWithReference(reference: Event.Reference) = context.parent == reference
fun Event.descendsFrom(event: Event) = descendsFromEventWithReference(event.reference)

fun Event.originatesFromEventWithReference(reference: Event.Reference) = context.originating == reference
fun Event.originatesFrom(event: Event) = originatesFromEventWithReference(event.reference)
