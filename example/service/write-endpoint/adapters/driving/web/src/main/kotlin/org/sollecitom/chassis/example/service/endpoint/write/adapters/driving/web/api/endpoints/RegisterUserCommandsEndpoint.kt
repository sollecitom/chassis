package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.endpoints

import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.UNPROCESSABLE_ENTITY
import org.http4k.format.auto
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.ContentNegotiation
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
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

    class V1(private val handle: suspend (RegisterUser.V1, InvocationContext<Access.Unauthenticated>) -> RegisterUser.V1.Result) : Endpoint {

        override val path = "/commands/${COMMAND_TYPE.name.value}/v${COMMAND_TYPE.version.value}"
        override val methods = setOf(Method.POST)

        override val route = routes(
            acceptCommand()
        )

        private fun acceptCommand() = path bind Method.POST toSuspending { request ->

            logger.debug { "Received command with type $COMMAND_TYPE" }
            val command = command(request)
            val context = unauthenticatedContext(request)

            val result = handle(command, context)

            result.toHttpResponse()
        }

        private fun RegisterUser.V1.Result.toHttpResponse(): Response = when (this) {
            is RegisterUser.V1.Result.Accepted -> Response(Status.ACCEPTED).body(this, RegisterUser.V1.Result.Accepted.serde)
            is RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse -> Response(UNPROCESSABLE_ENTITY.description("Email address is already used by another user"))
        }

        companion object : Loggable() {

            private val COMMAND_TYPE: Command.Type = RegisterUser.V1.Type

            // TODO use the swagger spec to create a Lens that validates the request against Swagger
            private val commandJson = Body.jsonObject().map(RegisterUser.V1.serde).toLens()
            private val negotiator = ContentNegotiation.auto(commandJson)
            private val command = negotiator.toBodyLens()
            private val unauthenticatedContext: BiDiBodyLens<InvocationContext<Access.Unauthenticated>> = TODO("implement") // TODO move
            private val authenticatedContext: BiDiBodyLens<InvocationContext<Access.Authenticated>> = TODO("implement") // TODO move
            private val context: BiDiBodyLens<InvocationContext<Access>> = TODO("implement") // TODO move
        }
    }
}