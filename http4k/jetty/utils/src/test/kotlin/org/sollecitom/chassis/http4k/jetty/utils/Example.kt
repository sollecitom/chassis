package org.sollecitom.chassis.http4k.jetty.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.server.asServer
import org.sollecitom.chassis.http4k.jetty.utils.loom.JettyLoom
import org.sollecitom.chassis.http4k.jetty.utils.loom.LoomThreadPool

fun main() {

    val server = ::testApp.asServer(JettyLoom(9000, LoomThreadPool())).start()
//    server.stop()
}

fun testApp(request: Request): Response = runBlocking {

    delay(50)
//    Thread.sleep(50) // simulating some async operation without cpu load
    Response(Status.OK).body("Hello, ${request.query("name")}!")
}