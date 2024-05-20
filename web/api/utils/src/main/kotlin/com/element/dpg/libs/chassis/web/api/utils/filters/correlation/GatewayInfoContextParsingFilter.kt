package com.element.dpg.libs.chassis.web.api.utils.filters.correlation

import com.element.dpg.libs.chassis.correlation.core.domain.context.InvocationContext
import com.element.dpg.libs.chassis.correlation.core.serialization.json.context.jsonSerde
import com.element.dpg.libs.chassis.web.api.utils.api.HttpApiDefinition
import com.element.dpg.libs.chassis.web.api.utils.api.withInvocationContext
import org.http4k.core.*
import org.http4k.core.cookie.cookies
import org.json.JSONObject

fun InvocationContextFilter.parseInvocationContextFromGatewayHeader(headerNames: com.element.dpg.libs.chassis.web.api.utils.headers.HttpHeaderNames.Correlation): Filter = GatewayInfoContextParsingFilter(key, headerNames)

context(HttpApiDefinition)
fun InvocationContextFilter.parseInvocationContextFromGatewayHeader(): Filter = parseInvocationContextFromGatewayHeader(headerNames.correlation)

context(HttpApiDefinition)
fun InvocationContextFilter.parseInvocationContextFromRequest(): Filter = parseInvocationContextFromRequest(headerNames)

fun InvocationContextFilter.parseInvocationContextFromRequest(headerNames: com.element.dpg.libs.chassis.web.api.utils.headers.HttpHeaderNames): Filter = GatewayInvocationContextFilter(headerNames)

internal class GatewayInfoContextParsingFilter(private val key: InvocationContextFilter.Key, private val headerNames: com.element.dpg.libs.chassis.web.api.utils.headers.HttpHeaderNames.Correlation) : Filter {

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
            context.access.isAuthenticated -> with(key.generic of context, key.optional of context, key.authenticated of (context as InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated>))
            else -> with(key.generic of context, key.optional of context, key.unauthenticated of (context as InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated>))
        }
    }

    private fun Throwable.asResponse() = Response(Status.BAD_REQUEST.description("Error while parsing the invocation context: $message"))

    private fun invocationContext(request: Request, headerNames: com.element.dpg.libs.chassis.web.api.utils.headers.HttpHeaderNames.Correlation): InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access>? {

        val rawValue = request.rawInvocationContextValue(headerNames) ?: return null
        val jsonValue = runCatching { JSONObject(rawValue) }.getOrElse { error("Invalid value for header ${headerNames.invocationContext}. Must be a JSON object.") }
        return InvocationContext.jsonSerde.deserialize(jsonValue)
    }

    private fun Request.rawInvocationContextValue(headerNames: com.element.dpg.libs.chassis.web.api.utils.headers.HttpHeaderNames.Correlation): String? {

        // TODO the comma in the JSON payload ends up producing multiple header values - fix this with Base64 or something, and go back to request.header(headerNames.invocationContext)
        return headers.filter { it.first == headerNames.invocationContext }.map { it.second }.takeUnless { it.isEmpty() }?.joinToString()
    }
}

internal class GatewayInvocationContextFilter(override val headerNames: com.element.dpg.libs.chassis.web.api.utils.headers.HttpHeaderNames) : Filter, HttpApiDefinition {

    override fun invoke(next: HttpHandler) = { request: Request ->

        val invocationContextParseResult = runCatching { invocationContext(request, headerNames.gateway) }
        when {
            invocationContextParseResult.isSuccess -> next(request.withInvocationContext(invocationContextParseResult.getOrThrow()))
            else -> invocationContextParseResult.exceptionOrNull()!!.asResponse()
        }
    }

    private fun invocationContext(request: Request, gatewayHeaderNames: com.element.dpg.libs.chassis.web.api.utils.headers.HttpHeaderNames.Gateway): InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access> {

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
        TODO("implement")
    }

    private fun Throwable.asResponse() = Response(Status.BAD_REQUEST.description("Error while parsing the invocation context: $message"))
}