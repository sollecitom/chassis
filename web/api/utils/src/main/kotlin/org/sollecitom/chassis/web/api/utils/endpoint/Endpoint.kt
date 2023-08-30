package org.sollecitom.chassis.web.api.utils.endpoint

import org.http4k.core.Method
import org.http4k.routing.RoutingHttpHandler

interface Endpoint {

    val path: String
    val methods: Set<Method>
    val route: RoutingHttpHandler
}