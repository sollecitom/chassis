package org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.specifications

import org.http4k.core.Body
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.format.auto
import org.http4k.lens.ContentNegotiation
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.CommandHandler
import org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.serde.serde
import org.sollecitom.chassis.example.command_endpoint.application.user.RegisterUser
import org.sollecitom.chassis.http4k.utils.lens.body
import org.sollecitom.chassis.http4k.utils.lens.jsonObject
import org.sollecitom.chassis.http4k.utils.lens.map

data object RegisterUserV1CommandHandler : CommandHandler<RegisterUser.V1, RegisterUser.V1.Result, Access.Unauthenticated> {

    private val commandJson = Body.jsonObject().map(RegisterUser.V1.serde).toLens()
    private val negotiator = ContentNegotiation.auto(commandJson)
    private val command = negotiator.toBodyLens()

    override val commandType = RegisterUser.V1.type

    override fun parseCommand(request: Request) = command(request)

    override fun resultToResponse(result: RegisterUser.V1.Result) = when (result) {
        is RegisterUser.V1.Result.Accepted -> Response(Status.ACCEPTED).body(result, RegisterUser.V1.Result.Accepted.serde)
        is RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse -> Response(Status.UNPROCESSABLE_ENTITY.description("Email address is already used by another user")) // TODO change to OK or something
    }

    override fun isResultAcceptable(result: Any?) = result is RegisterUser.V1.Result
}