package org.sollecitom.chassis.http4k.jetty.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import org.http4k.client.ApacheClient
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.server.asServer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.http4k.jetty.utils.loom.JettyLoom
import org.sollecitom.chassis.http4k.jetty.utils.loom.LoomThreadPool

@TestInstance(PER_CLASS)
private class ExampleTests {

    @Test
    fun `sending a request to a server and getting a response`() {

        var isServerThreadVirtual = false

        fun testApp(request: Request): Response {

            isServerThreadVirtual = Thread.currentThread().isVirtual
            Thread.sleep(1) // simulating some async operation without cpu load
            return Response(OK).body("Hello, ${request.query("name")}!")
        }

        val server = ::testApp.asServer(JettyLoom(0, LoomThreadPool())).start()
        val client = ApacheClient()
        val request = Request(Method.GET, "http://localhost:${server.port()}").query("name", "John Doe")

        val response = client(request)
        assertThat(response.status).isEqualTo(OK)
        assertThat(isServerThreadVirtual).isTrue()
        server.stop()
    }
}