package org.sollecitom.chassis.example.gateway.adapters.driving.http

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.*
import org.json.JSONObject
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.networking.Port
import org.sollecitom.chassis.core.domain.networking.RequestedPort
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.serialization.json.context.jsonSerde
import org.sollecitom.chassis.http4k.utils.lens.body
import org.sollecitom.chassis.json.test.utils.compliesWith
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.logging.standard.configuration.configureLogging
import org.sollecitom.chassis.test.utils.assertions.succeeded
import org.sollecitom.chassis.web.api.test.utils.LocalHttpDrivingAdapterTestSpecification
import org.sollecitom.chassis.web.api.utils.api.HttpApiDefinition
import org.sollecitom.chassis.web.api.utils.api.HttpDrivingAdapter
import org.sollecitom.chassis.web.api.utils.api.mainHttpApi
import org.sollecitom.chassis.web.api.utils.endpoint.Endpoint
import org.sollecitom.chassis.web.api.utils.filters.GatewayHttpFilter
import org.sollecitom.chassis.web.api.utils.headers.HttpHeaderNames
import org.sollecitom.chassis.web.api.utils.headers.of

@TestInstance(PER_CLASS) // TODO finish
private class GatewayHttpContractTests : HttpApiDefinition, LocalHttpDrivingAdapterTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val headerNames = HttpHeaderNames.of(companyName = "acme")

    init {
        configureLogging(defaultMinimumLoggingLevel = LoggingLevel.INFO)
    }

    @Disabled // TODO enable this and make it pass
    @Test
    fun `getting the contract right`() { // TODO add JWT to this, as header, and parse Access, etc. from it (use the jwt modules in Chassis)

        val downStreamResponse = Response(OK).header("Secret header", "secret header value")
        var downstreamRequest: Request? = null
        val downstreamRoute = { _: Request -> true }.asRouter().bind { request ->
            downstreamRequest = request
            downStreamResponse
        }
        val gateway = gateway(downstreamRoute)
        val json = JSONObject().put("key", "value")
        val request = Request(Method.POST, path("something")).body(json)

        val response = gateway(request)

        assertThat(response).isEqualTo(downStreamResponse)
        assertThat(downstreamRequest).wasRoutedWithInvocationContext()
    }

    private fun gateway(vararg downstreamRoutes: RoutingHttpHandler): HttpHandler = HttpDrivingAdapter(configuration = GatewayConfiguration(HttpDrivingAdapter.Configuration(requestedPort = RequestedPort.randomAvailable), headerNames), downstreamRoutes.toSet())

    context(HttpApiDefinition)
    private val Request.invocationContextHeaderOrNull
        get() = header(headerNames.correlation.invocationContext)

    context(HttpApiDefinition)
    private val Request.invocationContextHeader
        get() = invocationContextHeaderOrNull ?: error("No invocation context header '${headerNames.correlation.invocationContext}' present")

    private fun String.toJSON() = JSONObject(this)

    // TODO pass here additional expectations about the context
    private fun Assert<Request?>.wasRoutedWithInvocationContext() = given { request ->

        assertThat(request).isNotNull()
        request!!
        assertThat(request.invocationContextHeader).isNotNull()
        assertThat(request.invocationContextHeader.toJSON()).compliesWith(InvocationContext.jsonSerde.schema)
        val deserializationAttempt = runCatching {
            InvocationContext.jsonSerde.deserialize(request.invocationContextHeader.toJSON())
        }
        assertThat(deserializationAttempt).succeeded()
        // TODO assert some more on the context itself
    }
}

operator fun HttpDrivingAdapter.Companion.invoke(configuration: GatewayConfiguration, downstreamRoutes: Set<RoutingHttpHandler>): HttpDrivingAdapter = GatewayHttpDrivingAdapter(configuration, downstreamRoutes)

data class GatewayConfiguration(val http: HttpDrivingAdapter.Configuration, override val headerNames: HttpHeaderNames) : HttpApiDefinition

class GatewayHttpDrivingAdapter(private val configuration: GatewayConfiguration, private val downstreamRoutes: Set<RoutingHttpHandler>) : HttpDrivingAdapter, HttpApiDefinition by configuration {

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

    private fun mainHttpApi() = mainHttpApi(endpoints = setOf(RoutingEndpoint(downstreamRoutes)), requestFilter = GatewayHttpFilter.forRequests(), responseFilter = GatewayHttpFilter.forResponses(), requestedPort = configuration.http.requestedPort)

    companion object : Loggable()
}

class RoutingEndpoint(private val downstreamRoutes: Set<RoutingHttpHandler>) : Endpoint {

    private val pathTemplate = "path"
    override val path = "/{$pathTemplate}"
    override val methods = Method.entries.toSortedSet()
    private val router: Router = { _: Request -> true }.asRouter()
    override val route: RoutingHttpHandler = routes(router.bind(Handler()))
    private val downstreamRoute: RoutingHttpHandler = routes(*downstreamRoutes.toTypedArray())

    private inner class Handler : HttpHandler {

        override fun invoke(request: Request): Response {

            // TODO add logging, etc.
            return downstreamRoute(request)
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