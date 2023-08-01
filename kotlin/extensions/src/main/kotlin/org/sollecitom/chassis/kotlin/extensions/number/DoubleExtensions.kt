package org.sollecitom.chassis.kotlin.extensions.number

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs

fun Double.rounded(decimalPlace: Int, roundingMode: RoundingMode = RoundingMode.HALF_UP): Double {

    val bd = BigDecimal.valueOf(this).setScale(decimalPlace, roundingMode)
    return bd.toDouble()
}

fun Double.isEqualTo(other: Double, epsilon: Double): Boolean = abs(this - other) <= epsilon