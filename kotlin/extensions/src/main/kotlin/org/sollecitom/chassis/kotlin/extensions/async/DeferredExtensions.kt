package org.sollecitom.chassis.kotlin.extensions.async

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.future.await
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withTimeout
import java.util.concurrent.CompletionStage
import kotlin.time.Duration

suspend fun <V> Collection<Deferred<V>>.awaitAny(predicate: (V) -> Boolean = { true }): V? = if (isEmpty()) null else
    select {
        forEach { deferred ->
            deferred.onAwait { result ->
                if (predicate(result)) result
                else (this@awaitAny - deferred).awaitAny(predicate)
            }
        }
    }.also { filter { task -> task.isActive }.forEach { it.cancelAndJoin() } }

suspend fun <VALUE> Deferred<VALUE>.await(timeout: Duration): VALUE = withTimeout(timeout) { await() }

suspend fun <VALUE> CompletionStage<VALUE>.await(timeout: Duration): VALUE = withTimeout(timeout) { await() }

suspend fun Job.join(timeout: Duration) = withTimeout(timeout) { join() }

suspend fun Job.cancelAndJoin(timeout: Duration) = withTimeout(timeout) { cancelAndJoin() }