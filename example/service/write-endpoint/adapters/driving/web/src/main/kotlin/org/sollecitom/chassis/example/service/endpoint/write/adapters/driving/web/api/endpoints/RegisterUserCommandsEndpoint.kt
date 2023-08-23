package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.endpoints

import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.format.auto
import org.http4k.lens.ContentNegotiation
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.Endpoint
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.serde.serde
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser
import org.sollecitom.chassis.http4k.server.utils.toSuspending
import org.sollecitom.chassis.http4k.utils.lens.body
import org.sollecitom.chassis.http4k.utils.lens.jsonObject
import org.sollecitom.chassis.http4k.utils.lens.map
import org.sollecitom.chassis.logger.core.loggable.Loggable

sealed class RegisterUserCommandsEndpoint {

    class V1(private val handle: suspend (RegisterUser.V1) -> RegisterUser.V1.Result) : Endpoint {

        override val path = "/commands/${COMMAND_TYPE.id.value}/v${COMMAND_TYPE.version.value}"
        override val methods = setOf(Method.POST)

        override val route = routes(
            acceptCommand()
        )

        private fun acceptCommand() = path bind Method.POST toSuspending { request ->

            logger.debug { "Received command with type $COMMAND_TYPE" }
            val command = command(request)

            val result = handle(command)

            result.toHttpResponse()
        }

        private fun RegisterUser.V1.Result.toHttpResponse(): Response = when (this) {
            is RegisterUser.V1.Result.Accepted -> Response(Status.ACCEPTED).body(this, RegisterUser.V1.Result.Accepted.serde)
            is RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse -> Response(Status.UNPROCESSABLE_ENTITY).body("Email address is already used by another user") // TODO switch to JSON body
        }

        companion object : Loggable() {

            private val COMMAND_TYPE: Command.Type = RegisterUser.V1.Type

            // TODO use the swagger spec to create a Lens that validates the request against Swagger
            private val commandJson = Body.jsonObject().map(RegisterUser.V1.serde).toLens()
            private val negotiator = ContentNegotiation.auto(commandJson)
            private val command = negotiator.toBodyLens()
        }
    }
}