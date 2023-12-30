package org.sollecitom.chassis.ddd.application.dispatching

import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.application.ApplicationCommand
import org.sollecitom.chassis.ddd.application.LoggingApplicationAdapter
import org.sollecitom.chassis.logger.core.loggable.Loggable

private class DispatchingApplication(handlers: Set<ApplicationCommandHandler<*, *, *>>) : Application {

    private val handlerByType = handlers.associateBy(ApplicationCommandHandler<*, *, *>::commandType)

    context(InvocationContext<ACCESS>)
    @Suppress("UNCHECKED_CAST")
    override suspend fun <RESULT, ACCESS : Access> invoke(command: ApplicationCommand<RESULT, ACCESS>): RESULT {


        val handler = handlerByType[command.type]?.let { it as ApplicationCommandHandler<ApplicationCommand<RESULT, ACCESS>, RESULT, ACCESS> } ?: error("No handler for command with type ${command.type}. This should never happen.")
        return with(this@InvocationContext) { handler.process(command) }
    }

    companion object : Loggable()
}

operator fun Application.Companion.invoke(handlers: Set<ApplicationCommandHandler<*, *, *>> = emptySet()): Application = DispatchingApplication(handlers).let(::LoggingApplicationAdapter)

operator fun Application.Companion.invoke(vararg handlers: ApplicationCommandHandler<*, *, *>): Application = Application(handlers.toSet())