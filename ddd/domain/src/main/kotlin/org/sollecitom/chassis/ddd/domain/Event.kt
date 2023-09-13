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

    fun forkContext(invocation: InvocationTrace): Context = Context(invocation = context.invocation.fork(invocation), parent = reference, originating = context.originating ?: reference)

    data class Reference(override val id: Id, val type: Happening.Type, override val timestamp: Instant) : Timestamped, Identifiable {

        companion object
    }

    data class Context(val invocation: InvocationContext<Access>, val parent: Reference?, val originating: Reference?) {

        companion object
    }

    companion object
}

context(InvocationContext<*>)
fun Event.forkContext(): Event.Context = forkContext(invocation = this@InvocationContext.trace.invocation)

fun InvocationContext<*>.toEventContext(parentEvent: Event.Reference? = null, originatingEvent: Event.Reference? = null): Event.Context = Event.Context(this, parentEvent, originatingEvent)
