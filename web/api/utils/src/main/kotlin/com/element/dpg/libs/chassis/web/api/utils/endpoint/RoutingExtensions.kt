package com.element.dpg.libs.chassis.web.api.utils.endpoint

import com.element.dpg.libs.chassis.correlation.core.domain.context.InvocationContext
import com.element.dpg.libs.chassis.web.api.utils.filters.correlation.InvocationContextFilter
import kotlinx.coroutines.runBlocking
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.routing.PathMethod
import org.http4k.routing.RoutingHttpHandler

infix fun PathMethod.toAuthenticated(action: suspend InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated>.(request: Request) -> Response): RoutingHttpHandler = to { request ->

    val context = InvocationContextFilter.key.authenticated(request)
    runBlocking { with(context) { action(request) } }
}

infix fun PathMethod.toUnauthenticated(action: suspend InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated>.(request: Request) -> Response): RoutingHttpHandler = to { request ->

    val context = InvocationContextFilter.key.unauthenticated(request)
    runBlocking { with(context) { action(request) } }
}

infix fun PathMethod.toWithInvocationContext(action: suspend InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access>.(request: Request) -> Response): RoutingHttpHandler = to { request ->

    val context = InvocationContextFilter.key.generic(request)
    runBlocking { with(context) { action(request) } }
}