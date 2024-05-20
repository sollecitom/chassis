package com.element.dpg.libs.chassis.logger.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.slf4j.MDC
import com.element.dpg.libs.chassis.logger.core.implementation.ImmutableLoggingContext

suspend inline fun <T> withCoroutineLoggingContext(map: Map<String, String?>, restorePrevious: Boolean = true, noinline body: suspend CoroutineScope.() -> T): T = withLoggingContext(map, restorePrevious) { withContext(MDCContext(), body) }

inline fun <T> withLoggingContext(map: Map<String, String?>, restorePrevious: Boolean = true, body: () -> T): T {

    val cleanupCallbacks = map.map {
        val mdcValue = MDC.get(it.key)
        if (restorePrevious && mdcValue != null) {
            { MDC.put(it.key, mdcValue) }
        } else {
            { MDC.remove(it.key) }
        }
    }

    try {
        map.forEach {
            if (it.value != null) {
                MDC.put(it.key, it.value)
            }
        }
        return body()
    } finally {
        cleanupCallbacks.forEach { it.invoke() }
    }
}

fun LoggingContext.Companion.withEntries(entries: Map<String, String>): LoggingContext = ImmutableLoggingContext(entries)