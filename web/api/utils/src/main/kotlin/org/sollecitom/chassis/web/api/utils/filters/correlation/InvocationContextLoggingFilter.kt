package org.sollecitom.chassis.web.api.utils.filters.correlation

import kotlinx.coroutines.runBlocking
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.logger.core.withCoroutineLoggingContext

fun InvocationContextFilter.addInvocationContextToLoggingStack(): Filter = InvocationContextLoggingFilter()

internal class InvocationContextLoggingFilter : Filter {

    override fun invoke(next: HttpHandler) = { request: Request ->

        val context = InvocationContextFilter.key.optional(request)
        if (context != null) {
//            withContext(Dispatchers.VirtualThreads) {
            runBlocking {
                withCoroutineLoggingContext(context.toLoggingContext()) {
                    next(request)
                }
            }
//            }
        } else {
            next(request)
        }
    }
}

// TODO move
private fun InvocationContext<*>.toLoggingContext(): Map<String, String?> {

    // TODO fix this
    return mapOf("trace.invocation.id" to trace.invocation.id.stringValue)
}