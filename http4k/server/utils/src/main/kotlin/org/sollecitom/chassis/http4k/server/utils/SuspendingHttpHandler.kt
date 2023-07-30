package org.sollecitom.chassis.http4k.server.utils

import kotlinx.coroutines.runBlocking
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.routing.PathMethod
import org.http4k.routing.RoutingHttpHandler
import org.http4k.server.Http4kServer
import org.http4k.server.ServerConfig
import org.http4k.server.asServer

typealias SuspendingHttpHandler = suspend (request: Request) -> Response

fun SuspendingHttpHandler.asServer(config: ServerConfig): Http4kServer = asBlockingHandler().asServer(config)

fun SuspendingHttpHandler.asBlockingHandler(): HttpHandler = object : HttpHandler {

    override fun invoke(request: Request) = runBlocking { this@asBlockingHandler.invoke(request) }
}

infix fun PathMethod.toSuspending(action: SuspendingHttpHandler): RoutingHttpHandler = to(action.asBlockingHandler())