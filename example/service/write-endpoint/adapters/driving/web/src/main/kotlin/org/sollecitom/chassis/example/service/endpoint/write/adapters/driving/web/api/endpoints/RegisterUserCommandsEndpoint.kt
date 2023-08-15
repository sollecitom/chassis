package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.endpoints

import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.Endpoint
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser
import org.sollecitom.chassis.http4k.server.utils.toSuspending
import org.sollecitom.chassis.logger.core.loggable.Loggable

sealed class RegisterUserCommandsEndpoint {

    class V1 : Endpoint {

        override val path = "/commands/${COMMAND_TYPE.id.value}/${COMMAND_TYPE.version.value}"
        override val methods = setOf(Method.POST)

        override val route = routes(
            acceptCommand()
        )

        private fun acceptCommand() = path bind Method.POST toSuspending { request ->

            logger.debug { "Received command with type $COMMAND_TYPE" }
            Response(Status.ACCEPTED)
        }

        companion object : Loggable() {

            private val COMMAND_TYPE: Command.Type = RegisterUser.V1.Type
//        private val command = Body.jsonObject()
        }
    }
}

//internal class CommandsEndpoint : Endpoint { // TODO use this as template for RegisterUserCommandsEndpoint.V1
//
//    override val path = "/commands/{$COMMAND_TYPE_PATH_PARAM}/{$COMMAND_TYPE_VERSION_PATH_PARAM}"
//    override val methods = setOf(Method.POST)
//
//    override val route = routes(
//        acceptCommand()
//    )
//
//    private fun acceptCommand() = path bind Method.POST toSuspending { request ->
//
//        val commandType: Command.Type = commandType(request)
//        logger.debug { "Received command with type $commandType" }
//        Response(Status.ACCEPTED)
//    }
//
//    companion object : Loggable() {
//
//        private const val COMMAND_TYPE_PATH_PARAM = "command-type-name"
//        private const val COMMAND_TYPE_VERSION_PATH_PARAM = "command-type-version"
//        private val commandTypeName = Path.name().of(COMMAND_TYPE_PATH_PARAM)
//        private val commandTypeVersion = Path.int().map(::IntVersion, IntVersion::value).of(COMMAND_TYPE_VERSION_PATH_PARAM)
//        private val commandType = Path.composite("command-type") { SpecifiedCommandType(commandTypeName(it), commandTypeVersion(it)) }
////        private val command = Body.jsonObject()
//    }
//}