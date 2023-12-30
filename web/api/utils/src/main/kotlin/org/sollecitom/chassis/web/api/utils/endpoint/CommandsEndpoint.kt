package org.sollecitom.chassis.web.api.utils.endpoint

import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.lens.Path
import org.http4k.lens.int
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.correlation.logging.utils.log
import org.sollecitom.chassis.ddd.application.Application
import org.sollecitom.chassis.ddd.domain.Happening
import org.sollecitom.chassis.http4k.utils.lens.composite
import org.sollecitom.chassis.lens.core.extensions.naming.name
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.web.api.utils.command.handler.HttpCommandHandler

class CommandsEndpoint(private val application: Application, handlers: Set<HttpCommandHandler<*, *>>) : Endpoint {

    private val handlerByType = handlers.associateBy(HttpCommandHandler<*, *>::commandType)
    override val path = "/commands/{$COMMAND_TYPE_PATH_PARAM}/v{$COMMAND_TYPE_VERSION_PATH_PARAM}"
    override val methods = setOf(Method.POST)

    override val route = routes(
        acceptCommand()
    )

    private fun acceptCommand() = path bind Method.POST toWithInvocationContext { request ->

        val commandType = commandType(request)
        logger.log { "Received command with type $commandType" }

        val handler = handlerByType[commandType] ?: return@toWithInvocationContext commandType.unknown()
        val command = handler.parseCommand(request).takeUnless { it.requiresAuthentication && !access.isAuthenticated } ?: return@toWithInvocationContext commandType.unauthenticated()
        val result = application(command)
        handler.convertToResponse(result)
    }

    private fun Happening.Type.unknown() = Response(BAD_REQUEST.description("Command type with name: ${name.value} and version: ${version.value} is unknown")) // TODO create a function for this (maybe)

    private fun Happening.Type.unauthenticated() = Response(UNAUTHORIZED.description("Command type with name: ${name.value} and version: ${version.value} requires authentication")) // TODO create a function for this (maybe)

    companion object : Loggable() {

        private const val COMMAND_TYPE_PATH_PARAM = "command-type-name"
        private const val COMMAND_TYPE_VERSION_PATH_PARAM = "command-type-version"
        private val commandTypeName = Path.name().of(COMMAND_TYPE_PATH_PARAM)
        private val commandTypeVersion = Path.int().map(::IntVersion, IntVersion::value).of(COMMAND_TYPE_VERSION_PATH_PARAM)
        private val commandType = Path.composite("command-type") { Happening.Type(commandTypeName(it), commandTypeVersion(it)) }
    }
}

