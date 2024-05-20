package com.element.dpg.libs.chassis.web.api.utils.endpoint

import com.element.dpg.libs.chassis.core.domain.versioning.IntVersion
import com.element.dpg.libs.chassis.correlation.logging.utils.log
import com.element.dpg.libs.chassis.ddd.application.dispatching.Application
import com.element.dpg.libs.chassis.ddd.domain.Command
import com.element.dpg.libs.chassis.ddd.domain.Happening
import com.element.dpg.libs.chassis.ddd.domain.ifInvalid
import com.element.dpg.libs.chassis.http4k.utils.lens.composite
import com.element.dpg.libs.chassis.lens.core.extensions.naming.name
import com.element.dpg.libs.chassis.logger.core.loggable.Loggable
import com.element.dpg.libs.chassis.web.api.utils.command.handler.HttpCommandHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.core.Status.Companion.UNPROCESSABLE_ENTITY
import org.http4k.lens.Path
import org.http4k.lens.int
import org.http4k.routing.bind
import org.http4k.routing.routes

class CommandsEndpoint(private val application: Application, handlers: Set<HttpCommandHandler<*, *>>) : Endpoint {

    private val handlerByType = handlers.associateBy(HttpCommandHandler<*, *>::commandType)
    override val path = "/commands/{$COMMAND_TYPE_PATH_PARAM}/v{$COMMAND_TYPE_VERSION_PATH_PARAM}"
    override val methods = setOf(Method.POST)

    override val route = routes(acceptCommand())

    private fun acceptCommand() = path bind Method.POST toWithInvocationContext { request ->

        val commandType = commandType(request)
        logger.log { "Received command with type $commandType" }

        val handler = handlerByType[commandType] ?: return@toWithInvocationContext unknown(commandType)
        val command = handler.parseCommand(request)
        command.accessRequirements.check(this@toWithInvocationContext).ifInvalid { return@toWithInvocationContext it.toResponse(commandType) }
        val result = application(command)
        handler.convertToResponse(result)
    }

    private fun Command.AccessRequirements.Result.Invalid.toResponse(type: Happening.Type): Response = when (this) {
        Command.AccessRequirements.Result.Invalid.Authenticated -> Response(UNPROCESSABLE_ENTITY.description("Command type with name: ${type.name.value} and version: ${type.version.value} is only available with unauthenticated access")) // TODO create a function for this (maybe)
        Command.AccessRequirements.Result.Invalid.Unauthenticated -> Response(UNAUTHORIZED.description("Command type with name: ${type.name.value} and version: ${type.version.value} requires authentication")) // TODO create a function for this (maybe)
    }

    private fun unknown(type: Happening.Type) = Response(BAD_REQUEST.description("Command type with name: ${type.name.value} and version: ${type.version.value} is unknown")) // TODO create a function for this (maybe)

    companion object : Loggable() {

        private const val COMMAND_TYPE_PATH_PARAM = "command-type-name"
        private const val COMMAND_TYPE_VERSION_PATH_PARAM = "command-type-version"
        private val commandTypeName = Path.name().of(COMMAND_TYPE_PATH_PARAM)
        private val commandTypeVersion = Path.int().map(::IntVersion, IntVersion::value).of(COMMAND_TYPE_VERSION_PATH_PARAM)
        private val commandType = Path.composite("command-type") { Happening.Type(commandTypeName(it), commandTypeVersion(it)) }
    }
}

