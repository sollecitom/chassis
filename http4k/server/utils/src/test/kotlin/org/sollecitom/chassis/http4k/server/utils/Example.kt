package org.sollecitom.chassis.http4k.server.utils

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.server.JettyLoom

// TODO remove
fun main() {

    val server = ::suspendingTestApp.asServer(JettyLoom(9000)).start()
//    val server = ::testApp.asServer(JettyLoom(9000)).start()
//    server.stop()
}

suspend fun suspendingTestApp(request: Request): Response = coroutineScope {

    delay(50) // simulating some async operation without cpu load
    Response(Status.OK).body("Hello, ${request.query("name")}!")
}
