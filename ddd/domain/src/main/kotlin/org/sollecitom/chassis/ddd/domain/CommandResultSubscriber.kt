package org.sollecitom.chassis.ddd.domain

import kotlinx.coroutines.Deferred
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext

interface CommandResultSubscriber<in COMMAND : Command<RESULT, ACCESS>, out RESULT : Any, out ACCESS : Access> {

    context(InvocationContext<ACCESS>)
    fun resultForCommand(event: CommandWasReceived<COMMAND>): Deferred<RESULT>
}