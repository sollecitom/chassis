package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.endpoints

import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.Endpoint
import org.sollecitom.chassis.http4k.server.utils.toSuspending
import org.sollecitom.chassis.logger.core.loggable.Loggable

class CommandsEndpoint : Endpoint {

    override val path = "/commands"
    override val methods = setOf(Method.POST)

    override val route = path bind Method.POST toSuspending { request ->
        Response(Status.ACCEPTED)
    }

    companion object : Loggable()
}