package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api

import org.http4k.core.Method
import org.http4k.routing.RoutingHttpHandler

interface Endpoint { // TODO move?

    val path: String
    val methods: Set<Method>
    val route: RoutingHttpHandler
}