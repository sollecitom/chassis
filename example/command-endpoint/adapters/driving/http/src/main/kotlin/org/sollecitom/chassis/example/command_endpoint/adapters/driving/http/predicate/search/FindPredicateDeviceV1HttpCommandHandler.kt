package org.sollecitom.chassis.example.command_endpoint.adapters.driving.http.predicate.search

import org.http4k.core.Body
import org.http4k.core.Response
import org.http4k.core.Status
import org.sollecitom.chassis.example.event.domain.predicate.search.FindPredicateDevice
import org.sollecitom.chassis.http4k.utils.lens.jsonObject
import org.sollecitom.chassis.http4k.utils.lens.map
import org.sollecitom.chassis.web.api.utils.command.handler.HttpCommandHandler
import org.sollecitom.chassis.web.api.utils.command.handler.HttpCommandHandlerTemplate

private data object FindPredicateDeviceV1HttpCommandHandler : HttpCommandHandlerTemplate<FindPredicateDevice, FindPredicateDevice.Result>(FindPredicateDevice.type, FindPredicateDevice.Result::class, listOf(Body.jsonObject().map(FindPredicateDevice.serde))) {

    override fun resultToResponse(result: FindPredicateDevice.Result) = when (result) {
        is FindPredicateDevice.Result.Accepted -> Response(Status.ACCEPTED)
        is FindPredicateDevice.Result.Rejected.DisallowedEmailAddress -> Response(Status.UNPROCESSABLE_ENTITY.description(result.explanation)) // TODO change to OK or something
    }
}

val FindPredicateDevice.Companion.httpCommandHandler: HttpCommandHandler<FindPredicateDevice, FindPredicateDevice.Result> get() = FindPredicateDeviceV1HttpCommandHandler