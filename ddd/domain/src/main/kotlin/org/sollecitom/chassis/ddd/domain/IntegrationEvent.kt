package org.sollecitom.chassis.ddd.domain

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.traits.Identifiable
import org.sollecitom.chassis.core.domain.traits.Timestamped
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace

data class IntegrationEvent(val value: Event, val context: Context) : Event by value {

    val reference = Reference(id, type, timestamp)

    fun forkContext(invocation: InvocationTrace): Context = Context(invocation = context.invocation.fork(invocation), parent = reference, originating = context.originating ?: reference)

    data class Reference(override val id: Id, val type: Event.Type, override val timestamp: Instant) : Timestamped, Identifiable {

        companion object
    }

    data class Context(val invocation: InvocationContext<Access>, val parent: Reference?, val originating: Reference?) {

        companion object
    }

    companion object
}

fun Event.asIntegrationEvent(context: IntegrationEvent.Context): IntegrationEvent = IntegrationEvent(this, context)

fun <ACCESS : Access> Event.asIntegrationEvent(context: InvocationContext<ACCESS>): IntegrationEvent = IntegrationEvent(this, context.toEventContext())

context(InvocationContext<*>)
fun <EVENT : Event> EVENT.asIntegrationEvent(): IntegrationEvent = IntegrationEvent(this, toEventContext())

fun InvocationContext<*>.toEventContext(parentEvent: IntegrationEvent.Reference? = null, originatingEvent: IntegrationEvent.Reference? = null): IntegrationEvent.Context = IntegrationEvent.Context(this, parentEvent, originatingEvent)