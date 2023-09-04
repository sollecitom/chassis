package org.sollecitom.chassis.ddd.application

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.traits.Identifiable
import org.sollecitom.chassis.core.domain.traits.Timestamped
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace
import org.sollecitom.chassis.ddd.domain.Event

data class ApplicationEvent<out EVENT : Event, out ACCESS : Access>(val value: EVENT, val context: Context<ACCESS>) : Event by value {

    val reference = Reference(id, type, timestamp)

    fun forkContext(invocation: InvocationTrace): Context<ACCESS> = Context(invocation = context.invocation.fork(invocation), parent = reference, originating = context.originating ?: reference)

    data class Reference(override val id: Id, val type: Event.Type, override val timestamp: Instant) : Timestamped, Identifiable {

        companion object
    }

    data class Context<out ACCESS : Access>(val invocation: InvocationContext<ACCESS>, val parent: Reference?, val originating: Reference?) {

        companion object
    }

    companion object
}

fun <EVENT : Event, ACCESS : Access> EVENT.asApplicationEvent(context: ApplicationEvent.Context<ACCESS>): ApplicationEvent<EVENT, ACCESS> = ApplicationEvent(this, context)

fun <EVENT : Event, ACCESS : Access> EVENT.asApplicationEvent(context: InvocationContext<ACCESS>): ApplicationEvent<EVENT, ACCESS> = ApplicationEvent(this, context.toEventContext())

context(InvocationContext<ACCESS>)
fun <EVENT : Event, ACCESS : Access> EVENT.asApplicationEvent(): ApplicationEvent<EVENT, ACCESS> = ApplicationEvent(this, toEventContext())

fun <ACCESS : Access> InvocationContext<ACCESS>.toEventContext(parentEvent: ApplicationEvent.Reference? = null, originatingEvent: ApplicationEvent.Reference? = null): ApplicationEvent.Context<ACCESS> = ApplicationEvent.Context(this, parentEvent, originatingEvent)