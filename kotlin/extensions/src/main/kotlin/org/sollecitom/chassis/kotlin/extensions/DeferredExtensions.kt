package org.sollecitom.chassis.kotlin.extensions

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.selects.select

suspend fun <V> Collection<Deferred<V>>.awaitAny(predicate: (V) -> Boolean = { true }): V? = if (isEmpty()) null else
    select {
        forEach { deferred ->
            deferred.onAwait { result ->
                if (predicate(result)) result
                else (this@awaitAny - deferred).awaitAny(predicate)
            }
        }
    }.also { filter { task -> task.isActive }.forEach { it.cancelAndJoin() } }