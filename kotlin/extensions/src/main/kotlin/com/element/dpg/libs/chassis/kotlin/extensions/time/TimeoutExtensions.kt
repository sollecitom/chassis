package com.element.dpg.libs.chassis.kotlin.extensions.time

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlin.time.Duration

suspend fun <T> withTimeout(timeout: Duration?, action: suspend CoroutineScope.() -> T): T = timeout?.let { withTimeout(it, action) } ?: coroutineScope(action)