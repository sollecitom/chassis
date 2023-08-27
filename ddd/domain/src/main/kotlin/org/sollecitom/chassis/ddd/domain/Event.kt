package org.sollecitom.chassis.ddd.domain

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.traits.Identifiable
import org.sollecitom.chassis.core.domain.traits.Timestamped
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace

interface Event : Happening, Identifiable, Timestamped {

    val context: Context
    override val type: Type
    val reference: Reference get() = Reference(id, type, timestamp)

    companion object

    interface Type : Happening.Type {

        companion object
    }

    data class Reference(override val id: Id, val type: Type, override val timestamp: Instant) : Timestamped, Identifiable {

        companion object
    }

    data class Context(val invocation: InvocationContext<*>, val parent: Reference?, val originating: Reference?) {

        companion object
    }

    fun forkContext(invocation: InvocationTrace): Context = Context(invocation = context.invocation.fork(invocation), parent = reference, originating = context.originating ?: reference)
}

fun InvocationContext<*>.toEventContext(parentEvent: Event.Reference? = null, originatingEvent: Event.Reference? = null): Event.Context = Event.Context(this, parentEvent, originatingEvent)

