package com.element.dpg.libs.chassis.ddd.application.dispatching.dispatching

import com.element.dpg.libs.chassis.correlation.core.domain.access.Access
import com.element.dpg.libs.chassis.correlation.core.domain.context.InvocationContext
import com.element.dpg.libs.chassis.ddd.application.dispatching.Application
import com.element.dpg.libs.chassis.ddd.application.dispatching.LoggingApplicationAdapter
import com.element.dpg.libs.chassis.ddd.domain.Command
import com.element.dpg.libs.chassis.logger.core.loggable.Loggable

private class DispatchingApplication(handlers: Set<CommandHandler<*, *, *>>) : Application {

    private val handlerByType = handlers.associateBy(CommandHandler<*, *, *>::commandType)

    context(InvocationContext<ACCESS>)
    override suspend fun <RESULT, ACCESS : Access> invoke(command: Command<RESULT, ACCESS>): RESULT {

        val handler = handlerFor(command)
        return with(this@InvocationContext) { handler.process(command) }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <ACCESS : Access, RESULT> handlerFor(command: Command<RESULT, ACCESS>) = handlerByType[command.type]?.let { it as CommandHandler<Command<RESULT, ACCESS>, RESULT, ACCESS> } ?: error("No handler for command with type ${command.type}. This should never happen.")

    companion object : Loggable()
}

operator fun Application.Companion.invoke(handlers: Set<CommandHandler<*, *, *>> = emptySet()): Application = DispatchingApplication(handlers).let(::LoggingApplicationAdapter)

operator fun Application.Companion.invoke(vararg handlers: CommandHandler<*, *, *>): Application = Application(handlers.toSet())