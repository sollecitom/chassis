package org.sollecitom.chassis.example.gateway.adapters.driving.http

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.cookie.cookies
import org.http4k.routing.*
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.networking.Port
import org.sollecitom.chassis.core.domain.networking.RequestedPort
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.http4k.utils.lens.body
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.logging.standard.configuration.configureLogging
import org.sollecitom.chassis.web.api.test.utils.LocalHttpDrivingAdapterTestSpecification
import org.sollecitom.chassis.web.api.utils.api.HttpApiDefinition
import org.sollecitom.chassis.web.api.utils.api.HttpDrivingAdapter
import org.sollecitom.chassis.web.api.utils.api.mainHttpApi
import org.sollecitom.chassis.web.api.utils.endpoint.Endpoint
import org.sollecitom.chassis.web.api.utils.headers.HttpHeaderNames
import org.sollecitom.chassis.web.api.utils.headers.of

@TestInstance(PER_CLASS) // TODO finish
private class GatewayHttpContractTests : HttpApiDefinition, LocalHttpDrivingAdapterTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val headerNames = HttpHeaderNames.of(companyName = "acme")

    init {
        configureLogging(defaultMinimumLoggingLevel = LoggingLevel.INFO)
    }

    @Test
    fun `getting the contract right`() {

        // TODO inject routing configuration here
        // TODO inject the ability to perform a downstream request here
        val gateway = gateway()
        val json = JSONObject().put("key", "value")
        val request = Request(Method.POST, path("something")).body(json)

        val response = gateway(request)

        assertThat(response.status).isEqualTo(OK)
    }

    // TODO pass an application to this
    private fun gateway(): HttpHandler = HttpDrivingAdapter(configuration = GatewayConfiguration(HttpDrivingAdapter.Configuration(requestedPort = RequestedPort.randomAvailable), headerNames))
}

operator fun HttpDrivingAdapter.Companion.invoke(configuration: GatewayConfiguration): HttpDrivingAdapter = GatewayHttpDrivingAdapter(configuration)

data class GatewayConfiguration(val http: HttpDrivingAdapter.Configuration, override val headerNames: HttpHeaderNames) : HttpApiDefinition

class GatewayHttpDrivingAdapter(private val configuration: GatewayConfiguration) : HttpDrivingAdapter, HttpApiDefinition by configuration {

    private val api = mainHttpApi()
    override val port: Port get() = api.port

    override fun invoke(request: Request) = api(request)

    override suspend fun start() {

        api.start()
        logger.info { "Started on port ${api.port}" }
    }

    override suspend fun stop() {

        api.stop()
        logger.info { "Stopped" }
    }

    // TODO don't use the application here?
    private fun mainHttpApi() = mainHttpApi(endpoints = setOf(GatewayEndpoint()), requestedPort = configuration.http.requestedPort)

    companion object : Loggable()
}

class GatewayEndpoint : Endpoint {

    private val pathTemplate = "path"
    override val path = "/{$pathTemplate}"
    override val methods = Method.entries.toSortedSet()
    private val router: Router = { _: Request -> true }.asRouter()

    override val route: RoutingHttpHandler = routes(router.bind(Handler()))

    private inner class Handler : HttpHandler {

        override fun invoke(request: Request): Response {

            val host = request.uri.host
            val authority = request.uri.authority
            val scheme = request.uri.scheme
            val path = request.uri.path
            val query = request.uri.query
            val fragment = request.uri.fragment
            val userInfo = request.uri.userInfo
            println("host: $host")
            println("path: $path")
            println("authority: $authority")
            println("scheme: $scheme")
            println("query: $query")
            println("fragment: $fragment")
            println("userInfo: $userInfo")

            val method = request.method
            println("method: $method")
            val headers = request.headers.groupBy { it.first }.mapValues { it.value.map { param -> param.second } }
            println("headers: $headers")

            val version = request.version
//            val body = request.body
            val source = request.source // the origin IP address, etc.
            val cookies = request.cookies()
            return Response(OK)
        }
    }

    companion object : Loggable()
}

// TODO remaining test list
//- Missing originating action ID or originating invocation ID
//- Parsing the origin of the request (from the user agent)
//- Identifying the tenant (from the domain?)
//- Parsing the access (from the JWT) + caching
//- Routing based on configuration
//- Routing based on Swagger
//- Exposing a merged and adjusted single Swagger file, from a collection of multiple downstream Swagger files
//- Logging
//- Metrics
//- Performing the request and returning the response vs redirecting
//- Removing unwanted headers (e.g., replace JWT header with invocation-context header)