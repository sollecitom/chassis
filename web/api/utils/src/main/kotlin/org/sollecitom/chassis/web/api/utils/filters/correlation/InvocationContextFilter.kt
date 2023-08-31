package org.sollecitom.chassis.web.api.utils.filters.correlation

import org.http4k.lens.RequestContextKey
import org.http4k.lens.RequestContextLens
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.web.api.utils.filters.RequestContextsProvider

object InvocationContextFilter {

    private val requestContexts get() = RequestContextsProvider.requestContexts
    val key: Key by lazy { Key(generic = RequestContextKey.required(requestContexts), authenticated = RequestContextKey.required(requestContexts), unauthenticated = RequestContextKey.required(requestContexts)) }

    data class Key(val generic: RequestContextLens<InvocationContext<Access>>, val authenticated: RequestContextLens<InvocationContext<Access.Authenticated>>, val unauthenticated: RequestContextLens<InvocationContext<Access.Unauthenticated>>)

    // TODO add a filter that puts the context on the logging stack
    // TODO create 1 Filter that acts as an embedded gateway itself
}