package org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.user.registration

import org.http4k.core.Body
import org.http4k.core.Response
import org.http4k.core.Status
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.example.command_endpoint.application.user.registration.RegisterUser
import org.sollecitom.chassis.http4k.utils.lens.body
import org.sollecitom.chassis.http4k.utils.lens.jsonObject
import org.sollecitom.chassis.http4k.utils.lens.map
import org.sollecitom.chassis.web.api.utils.command.handler.HttpCommandHandlerTemplate

data object RegisterUserV1HttpCommandHandler : HttpCommandHandlerTemplate<RegisterUser.V1, RegisterUser.V1.Result, Access.Unauthenticated>(RegisterUser.V1.type, RegisterUser.V1.Result::class, listOf(Body.jsonObject().map(RegisterUser.V1.serde))) {

    override fun resultToResponse(result: RegisterUser.V1.Result) = when (result) {
        is RegisterUser.V1.Result.Accepted -> Response(Status.ACCEPTED).body(result, RegisterUser.V1.Result.Accepted.serde)
        is RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse -> Response(Status.UNPROCESSABLE_ENTITY.description("Email address is already used by another user")) // TODO change to OK or something
    }
}