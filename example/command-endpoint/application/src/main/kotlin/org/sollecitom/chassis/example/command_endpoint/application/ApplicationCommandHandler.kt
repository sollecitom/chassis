package org.sollecitom.chassis.example.command_endpoint.application

import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.application.ApplicationCommand
import org.sollecitom.chassis.ddd.domain.Happening

// TODO move
interface ApplicationCommandHandler<COMMAND : ApplicationCommand<RESULT, ACCESS>, RESULT, ACCESS : Access> {

    val commandType: Happening.Type

    context(InvocationContext<ACCESS>)
    suspend fun process(command: COMMAND): RESULT
}