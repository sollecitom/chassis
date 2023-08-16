package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.endpoints

import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.Path
import org.http4k.lens.int
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.Endpoint
import org.sollecitom.chassis.example.service.endpoint.write.application.GenericCommandType
import org.sollecitom.chassis.http4k.server.utils.toSuspending
import org.sollecitom.chassis.http4k.utils.lens.composite
import org.sollecitom.chassis.lens.core.extensions.naming.name
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.web.api.utils.HttpHeaders

internal class UnknownCommandsEndpoint : Endpoint {

    override val path = "/commands/{$COMMAND_TYPE_PATH_PARAM}/{$COMMAND_TYPE_VERSION_PATH_PARAM}"
    override val methods = setOf(Method.POST)

    override val route = routes(
        acceptCommand()
    )

    private fun acceptCommand() = path bind Method.POST toSuspending { request ->

        val commandType = commandType(request)
        logger.debug { "Received unknown command with type $commandType" }
        // TODO use an error JSON response body instead
        Response(Status.BAD_REQUEST).body("Unknown command type with name: ${commandType.id.value} and version: ${commandType.version.value}")
    }

    fun Response.body(json: JSONObject) = body(json.toString()).header(HttpHeaders.ContentType.name, ContentType.APPLICATION_JSON.toHeaderValue())

    companion object : Loggable() {

        private const val COMMAND_TYPE_PATH_PARAM = "command-type-name"
        private const val COMMAND_TYPE_VERSION_PATH_PARAM = "command-type-version"
        private val commandTypeName = Path.name().of(COMMAND_TYPE_PATH_PARAM)
        private val commandTypeVersion = Path.int().map(::IntVersion, IntVersion::value).of(COMMAND_TYPE_VERSION_PATH_PARAM)
        private val commandType = Path.composite("command-type") { GenericCommandType(commandTypeName(it), commandTypeVersion(it)) }
    }
}