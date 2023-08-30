package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api

import org.http4k.core.*
import org.http4k.lens.RequestContextKey
import org.http4k.lens.RequestContextLens
import org.json.JSONObject
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.serialization.json.context.jsonSerde
import org.sollecitom.chassis.web.api.utils.api.HttpApiDefinition
import org.sollecitom.chassis.web.api.utils.headers.HttpHeaderNames

// TODO move
object InvocationContextFilter {

    private val requestContexts get() = RequestContextsProvider.requestContexts
    val key: Key by lazy { Key(generic = RequestContextKey.required(requestContexts), authenticated = RequestContextKey.required(requestContexts), unauthenticated = RequestContextKey.required(requestContexts)) }

    data class Key(val generic: RequestContextLens<InvocationContext<Access>>, val authenticated: RequestContextLens<InvocationContext<Access.Authenticated>>, val unauthenticated: RequestContextLens<InvocationContext<Access.Unauthenticated>>)

    // TODO add a filter that puts the context on the logging stack
    // TODO create 1 Filter that acts as an embedded gateway itself
    context(WithCoreGenerators)
    fun parseContextFromGatewayHeaders(headerNames: HttpHeaderNames.Correlation): Filter = GatewayInfoContextParsingFilter(key, headerNames)

    context(WithCoreGenerators, HttpApiDefinition)
    fun parseContextFromGatewayHeaders(): Filter = parseContextFromGatewayHeaders(headerNames.correlation)

    context(WithCoreGenerators)
    private class GatewayInfoContextParsingFilter(private val key: Key, private val headerNames: HttpHeaderNames.Correlation) : Filter {

        override fun invoke(next: HttpHandler) = { request: Request ->

            val attempt = runCatching { invocationContext(request, headerNames) }
            when {
                attempt.isSuccess -> next(request.with(attempt.getOrThrow()))
                else -> attempt.exceptionOrNull()!!.asResponse()
            }
        }

        @Suppress("UNCHECKED_CAST")
        private fun Request.with(context: InvocationContext<*>?): Request {
            if (context == null) return this
            return when {
                context.access.isAuthenticated -> with(key.generic of context, key.authenticated of (context as InvocationContext<Access.Authenticated>))
                else -> with(key.generic of context, key.unauthenticated of (context as InvocationContext<Access.Unauthenticated>))
            }
        }

        private fun Throwable.asResponse() = Response(Status.BAD_REQUEST.description("Error while parsing the invocation context: $message"))

        context(WithCoreGenerators)
        private fun invocationContext(request: Request, headerNames: HttpHeaderNames.Correlation): InvocationContext<Access>? {

            val rawValue = request.header(headerNames.invocationContext) ?: return null
            val jsonValue = runCatching { JSONObject(rawValue) }.getOrElse { error("Invalid value for header ${headerNames.invocationContext}. Must be a JSON object.") }
            return InvocationContext.jsonSerde.deserialize(jsonValue)
        }
    }
}