package org.sollecitom.chassis.http4k.jetty.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.server.JettyLoom
import org.http4k.server.asServer

fun main() {

    val server = ::testApp.asServer(JettyLoom(0)).start()
//    server.stop()
}

fun testApp(request: Request): Response = runBlocking {

    delay(50)
//    Thread.sleep(50) // simulating some async operation without cpu load
    Response(Status.OK).body("Hello, ${request.query("name")}!")
}