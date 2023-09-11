package org.sollecitom.chassis.example.write_endpoint.adapters.driving.web.api.endpoints

import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.UNPROCESSABLE_ENTITY
import org.http4k.format.auto
import org.http4k.lens.ContentNegotiation
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.logging.utils.log
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.example.write_endpoint.adapters.driving.web.api.serde.serde
import org.sollecitom.chassis.example.write_endpoint.application.user.RegisterUser
import org.sollecitom.chassis.http4k.utils.lens.body
import org.sollecitom.chassis.http4k.utils.lens.jsonObject
import org.sollecitom.chassis.http4k.utils.lens.map
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.web.api.utils.endpoint.Endpoint
import org.sollecitom.chassis.web.api.utils.endpoint.toUnauthenticated

sealed class RegisterUserCommandsEndpoint {

    class V1(private val handle: suspend InvocationContext<Access.Unauthenticated>.(RegisterUser.V1) -> RegisterUser.V1.Result) : Endpoint {

        override val path = "/commands/${COMMAND_TYPE.name.value}/v${COMMAND_TYPE.version.value}"
        override val methods = setOf(Method.POST)

        override val route = routes(
            acceptCommand()
        )

        private fun acceptCommand() = path bind Method.POST toUnauthenticated { request ->

            logger.log { "Received command with type '$COMMAND_TYPE'" }
            val command = Companion.command(request)

            val result = handle(command)

            result.toHttpResponse()
        }

        private fun RegisterUser.V1.Result.toHttpResponse(): Response = when (this) {
            is RegisterUser.V1.Result.Accepted -> Response(Status.ACCEPTED).body(this, RegisterUser.V1.Result.Accepted.serde)
            is RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse -> Response(UNPROCESSABLE_ENTITY.description("Email address is already used by another user"))
        }

        companion object : Loggable() {

            private val COMMAND_TYPE: Command.Type = RegisterUser.V1.Type

            private val commandJson = Body.jsonObject().map(RegisterUser.V1.serde).toLens()
            private val negotiator = ContentNegotiation.auto(commandJson)
            private val command = negotiator.toBodyLens()
        }
    }
}