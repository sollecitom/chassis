package com.element.dpg.libs.chassis.kotlin.extensions.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration

inline fun CoroutineScope.schedulePeriodic(context: CoroutineContext = EmptyCoroutineContext, start: CoroutineStart = CoroutineStart.DEFAULT, delay: Duration = Duration.ZERO, period: Duration, crossinline action: suspend () -> Unit): Job {

    return launch(context = context, start = start) {
        delay(delay)
        while (true) {
            action()
            delay(period)
        }
    }
}