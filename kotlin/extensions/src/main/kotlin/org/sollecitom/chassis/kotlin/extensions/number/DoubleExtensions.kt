package org.sollecitom.chassis.kotlin.extensions.number

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs

fun Double.rounded(decimalPlace: Int, roundingMode: RoundingMode = RoundingMode.HALF_UP): Double {

    val bd = BigDecimal.valueOf(this).setScale(decimalPlace, roundingMode)
    return bd.toDouble()
}

private const val defaultDoubleTolerance = 0.00001

fun Double.isEqualToWithTolerance(other: Double, tolerance: Double = defaultDoubleTolerance): Boolean = abs(this - other) <= tolerance