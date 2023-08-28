package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api

import org.http4k.core.*
import org.http4k.lens.RequestContextKey
import org.http4k.lens.RequestContextLens
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.networking.IpAddress
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.authorization.AuthorizationInfo
import org.sollecitom.chassis.correlation.core.domain.authorization.Role
import org.sollecitom.chassis.correlation.core.domain.authorization.Roles
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.domain.origin.Origin
import org.sollecitom.chassis.correlation.core.domain.trace.ExternalInvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace
import org.sollecitom.chassis.correlation.core.domain.trace.Trace

// TODO move
object InvocationContextFilter {

    private val requestContexts get() = RequestContextsProvider.requestContexts
    val key: Key by lazy { Key(generic = RequestContextKey.required(requestContexts), authenticated = RequestContextKey.required(requestContexts), unauthenticated = RequestContextKey.required(requestContexts)) }

    data class Key(val generic: RequestContextLens<InvocationContext<Access>>, val authenticated: RequestContextLens<InvocationContext<Access.Authenticated>>, val unauthenticated: RequestContextLens<InvocationContext<Access.Unauthenticated>>)

    // TODO create 1 that acts as gateway itself
    context(WithCoreGenerators)
    fun parseContextFromGatewayHeaders(): Filter = GatewayInfoContextParsingFilter(key)

    context(WithCoreGenerators)
    private class GatewayInfoContextParsingFilter(private val key: Key) : Filter {

        override fun invoke(next: HttpHandler) = { request: Request ->

            val attempt = runCatching { invocationContext(request) }
            when {
                attempt.isSuccess -> next(request.with(attempt.getOrThrow()))
                else -> attempt.exceptionOrNull()!!.asResponse()
            }
        }

        @Suppress("UNCHECKED_CAST")
        private fun Request.with(context: InvocationContext<*>): Request = when {
            context.access.isAuthenticated -> with(key.generic of context, key.authenticated of (context as InvocationContext<Access.Authenticated>))
            else -> with(key.generic of context, key.unauthenticated of (context as InvocationContext<Access.Unauthenticated>))
        }

        private fun Throwable.asResponse() = Response(Status.BAD_REQUEST.description("Error while parsing the invocation context: $message"))

        context(WithCoreGenerators)
        private fun invocationContext(request: Request): InvocationContext<Access> {

            // TODO fix this to parse this information from headers
            val origin = Origin(IpAddress.create("127.0.0.1"))
            val authorization = AuthorizationInfo(roles = Roles(setOf(Role("admin".let(::Name)))))
            val invocation = InvocationTrace(id = newId.internal(), createdAt = clock.now())
            val parent = InvocationTrace(id = newId.internal(), createdAt = clock.now())
            val originating = InvocationTrace(id = newId.internal(), createdAt = clock.now())
            val external = ExternalInvocationTrace(invocationId = newId.external(), actionId = newId.external())
            val trace = Trace(invocation = invocation, parent = parent, originating = originating, external = external)
            return InvocationContext(Access.Unauthenticated(origin, authorization), trace)
        }
    }
}