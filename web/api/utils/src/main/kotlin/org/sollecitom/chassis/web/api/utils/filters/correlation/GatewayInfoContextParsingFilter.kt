package org.sollecitom.chassis.web.api.utils.filters.correlation

import org.http4k.core.*
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.serialization.json.context.jsonSerde
import org.sollecitom.chassis.web.api.utils.api.HttpApiDefinition
import org.sollecitom.chassis.web.api.utils.headers.HttpHeaderNames

fun InvocationContextFilter.parseInvocationContextFromGatewayHeader(headerNames: HttpHeaderNames.Correlation): Filter = GatewayInfoContextParsingFilter(key, headerNames)

context(HttpApiDefinition)
fun InvocationContextFilter.parseInvocationContextFromGatewayHeader(): Filter = parseInvocationContextFromGatewayHeader(headerNames.correlation)

internal class GatewayInfoContextParsingFilter(private val key: InvocationContextFilter.Key, private val headerNames: HttpHeaderNames.Correlation) : Filter {

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
            context.access.isAuthenticated -> with(key.generic of context, key.optional of context, key.authenticated of (context as InvocationContext<Access.Authenticated>))
            else -> with(key.generic of context, key.optional of context, key.unauthenticated of (context as InvocationContext<Access.Unauthenticated>))
        }
    }

    private fun Throwable.asResponse() = Response(Status.BAD_REQUEST.description("Error while parsing the invocation context: $message"))

    private fun invocationContext(request: Request, headerNames: HttpHeaderNames.Correlation): InvocationContext<Access>? {

        val rawValue = request.rawInvocationContextValue(headerNames) ?: return null
        val jsonValue = runCatching { JSONObject(rawValue) }.getOrElse { error("Invalid value for header ${headerNames.invocationContext}. Must be a JSON object.") }
        return InvocationContext.jsonSerde.deserialize(jsonValue)
    }

    private fun Request.rawInvocationContextValue(headerNames: HttpHeaderNames.Correlation): String? {

        // TODO the comma in the JSON payload ends up producing multiple header values - fix this with Base64 or something, and go back to request.header(headerNames.invocationContext)
        return headers.filter { it.first == headerNames.invocationContext }.map { it.second }.takeUnless { it.isEmpty() }?.joinToString()
    }
}