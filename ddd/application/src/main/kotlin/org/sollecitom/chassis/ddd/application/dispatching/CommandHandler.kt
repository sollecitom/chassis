package org.sollecitom.chassis.ddd.application.dispatching

import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.ddd.domain.Happening

interface CommandHandler<in COMMAND : Command<RESULT, ACCESS>, out RESULT, out ACCESS : Access> {

    val commandType: Happening.Type

    context(InvocationContext<ACCESS>)
    suspend fun process(command: COMMAND): RESULT
}