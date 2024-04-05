package org.sollecitom.chassis.ddd.domain

import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext

interface ReceivedCommandPublisher<in COMMAND : Command<*, ACCESS>, out ACCESS : Access> : Startable, Stoppable {

    context(InvocationContext<ACCESS>)
    suspend fun publish(event: CommandWasReceived<COMMAND>)
}