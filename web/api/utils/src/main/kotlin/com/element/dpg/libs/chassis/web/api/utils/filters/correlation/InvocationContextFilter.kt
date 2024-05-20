package com.element.dpg.libs.chassis.web.api.utils.filters.correlation

import org.http4k.lens.RequestContextKey
import org.http4k.lens.RequestContextLens
import com.element.dpg.libs.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.web.api.utils.filters.RequestContextsProvider

object InvocationContextFilter {

    private val requestContexts get() = RequestContextsProvider.requestContexts
    val key: Key by lazy { Key(generic = RequestContextKey.required(requestContexts), optional = RequestContextKey.optional(requestContexts), authenticated = RequestContextKey.required(requestContexts), unauthenticated = RequestContextKey.required(requestContexts)) }

    data class Key(val generic: RequestContextLens<InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access>>, val optional: RequestContextLens<InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access>?>, val authenticated: RequestContextLens<InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Authenticated>>, val unauthenticated: RequestContextLens<InvocationContext<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated>>)

    // TODO create 1 Filter that acts as an embedded gateway itself
}