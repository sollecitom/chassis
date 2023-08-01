package org.sollecitom.chassis.kotlin.extensions.throwables

val Throwable.causes: Sequence<Throwable>
    get() = sequence {
        val seenCauses = mutableSetOf<Throwable>()
        var cause: Throwable? = cause
        while (cause != null && cause !in seenCauses) {
            seenCauses += cause
            yield(cause)
            cause = cause.cause
        }
    }