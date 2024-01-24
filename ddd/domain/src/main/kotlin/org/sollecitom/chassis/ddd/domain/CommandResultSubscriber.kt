package org.sollecitom.chassis.ddd.domain

import kotlinx.coroutines.Deferred
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext

interface CommandResultSubscriber {

    context(InvocationContext<ACCESS>)
    fun <COMMAND : Command<RESULT, ACCESS>, RESULT : Any, ACCESS : Access> resultForCommand(event: CommandWasReceived<COMMAND>): Deferred<RESULT>
}