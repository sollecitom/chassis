package org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.user.registration

import org.http4k.core.Body
import org.http4k.core.Response
import org.http4k.core.Status
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.example.command_endpoint.application.user.registration.RegisterUser
import org.sollecitom.chassis.http4k.utils.lens.body
import org.sollecitom.chassis.http4k.utils.lens.jsonObject
import org.sollecitom.chassis.http4k.utils.lens.map
import org.sollecitom.chassis.web.api.utils.command.handler.HttpCommandHandler
import org.sollecitom.chassis.web.api.utils.command.handler.HttpCommandHandlerTemplate

private data object RegisterUserV1HttpCommandHandler : HttpCommandHandlerTemplate<RegisterUser, RegisterUser.Result>(RegisterUser.type, RegisterUser.Result::class, listOf(Body.jsonObject().map(RegisterUser.serde))) {

    override fun resultToResponse(result: RegisterUser.Result) = when (result) {
        is RegisterUser.Result.Accepted -> Response(Status.ACCEPTED).body(result, RegisterUser.Result.Accepted.serde)
        is RegisterUser.Result.Rejected.EmailAddressAlreadyInUse -> Response(Status.UNPROCESSABLE_ENTITY.description("Email address is already used by another user")) // TODO change to OK or something
    }
}

val RegisterUser.Companion.httpCommandHandler: HttpCommandHandler<RegisterUser, RegisterUser.Result> get() = RegisterUserV1HttpCommandHandler