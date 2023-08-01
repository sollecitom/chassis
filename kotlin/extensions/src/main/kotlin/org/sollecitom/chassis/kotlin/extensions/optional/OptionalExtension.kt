package org.sollecitom.chassis.kotlin.extensions.optional

import java.util.*

fun <VALUE> Optional<VALUE>.asNullable(): VALUE? = orElse(null)

fun <VALUE : Any> VALUE?.asOptional(): Optional<VALUE> = Optional.ofNullable(this)