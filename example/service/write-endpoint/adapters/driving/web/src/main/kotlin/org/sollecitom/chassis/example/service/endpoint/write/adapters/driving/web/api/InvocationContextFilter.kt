package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api

import org.http4k.core.*
import org.http4k.lens.RequestContextKey
import org.http4k.lens.RequestContextLens
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext

// TODO move
object InvocationContextFilter {

    private val requestContexts = RequestContexts()
    private val key: Key = Key(generic = RequestContextKey.required(requestContexts), authenticated = RequestContextKey.required(requestContexts), unauthenticated = RequestContextKey.required(requestContexts))

    data class Key(val generic: RequestContextLens<InvocationContext<Access>>, val authenticated: RequestContextLens<InvocationContext<Access.Authenticated>>, val unauthenticated: RequestContextLens<InvocationContext<Access.Unauthenticated>>)

    @Suppress("FunctionName")
    fun AddState(): Filter = AddStateFilter(key)

    private class AddStateFilter(private val key: Key) : Filter {

        override fun invoke(next: HttpHandler) = { request: Request ->

            val attempt = runCatching { invocationContext(request) }
            when {
                attempt.isSuccess -> next(request.with(attempt.getOrThrow()))
                else -> attempt.exceptionOrNull()!!.asResponse()
            }
        }

        @Suppress("UNCHECKED_CAST")
        private fun Request.with(context: InvocationContext<*>): Request = when {
            context.access.isAuthenticated -> with(key.generic of context, key.authenticated of context as InvocationContext<Access.Authenticated>)
            else -> with(key.generic of context, key.unauthenticated of context as InvocationContext<Access.Unauthenticated>)
        }

        private fun Throwable.asResponse() = Response(Status.BAD_REQUEST.description("Error while parsing the invocation context: $message"))

        private fun invocationContext(request: Request): InvocationContext<Access> {

            TODO("implement")
        }
    }
}