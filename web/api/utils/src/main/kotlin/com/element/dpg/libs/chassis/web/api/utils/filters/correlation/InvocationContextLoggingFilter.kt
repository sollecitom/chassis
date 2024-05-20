package com.element.dpg.libs.chassis.web.api.utils.filters.correlation

import com.element.dpg.libs.chassis.correlation.core.domain.context.InvocationContext
import com.element.dpg.libs.chassis.correlation.core.serialization.json.context.jsonSerde
import com.element.dpg.libs.chassis.logger.core.withCoroutineLoggingContext
import kotlinx.coroutines.runBlocking
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.json.JSONObject

fun InvocationContextFilter.addInvocationContextToLoggingStack(): Filter = InvocationContextLoggingFilter()

internal class InvocationContextLoggingFilter : Filter {

    override fun invoke(next: HttpHandler) = { request: Request ->

        val context = InvocationContextFilter.key.optional(request)
        if (context != null) {
            runBlocking {
                withCoroutineLoggingContext(context.toLoggingContext()) {
                    next(request)
                }
            }
        } else {
            next(request)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun InvocationContext<*>.toLoggingContext(): Map<String, String?> {

        val json = JSONObject().apply {
            put("invocation", InvocationContext.jsonSerde.serialize(this@toLoggingContext).toString())
        }
        return json.toMap() as Map<String, String?>
    }
}

