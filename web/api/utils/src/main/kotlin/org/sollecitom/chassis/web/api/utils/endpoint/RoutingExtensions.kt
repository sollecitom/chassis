package org.sollecitom.chassis.web.api.utils.endpoint

import kotlinx.coroutines.runBlocking
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.routing.PathMethod
import org.http4k.routing.RoutingHttpHandler
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.web.api.utils.filters.correlation.InvocationContextFilter

infix fun PathMethod.toAuthenticated(action: suspend InvocationContext<Access.Authenticated>.(request: Request) -> Response): RoutingHttpHandler = to { request ->

    val context = InvocationContextFilter.key.authenticated(request)
    runBlocking { with(context) { action(request) } }
}

infix fun PathMethod.toUnauthenticated(action: suspend InvocationContext<Access.Unauthenticated>.(request: Request) -> Response): RoutingHttpHandler = to { request ->

    val context = InvocationContextFilter.key.unauthenticated(request)
    runBlocking { with(context) { action(request) } }
}

infix fun PathMethod.toWithInvocationContext(action: suspend InvocationContext<Access>.(request: Request) -> Response): RoutingHttpHandler = to { request ->

    val context = InvocationContextFilter.key.generic(request)
    runBlocking { with(context) { action(request) } }
}