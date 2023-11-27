package org.sollecitom.chassis.kotlin.extensions.number

import kotlin.math.pow

fun Int.pow(exponent: Int): Int = toDouble().pow(exponent).toInt()

fun Long.pow(exponent: Int): Long = toDouble().pow(exponent).toLong()