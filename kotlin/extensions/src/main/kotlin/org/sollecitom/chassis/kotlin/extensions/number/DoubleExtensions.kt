package org.sollecitom.chassis.kotlin.extensions.number

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

fun Double.rounded(decimalPlace: Int, roundingMode: RoundingMode = RoundingMode.HALF_UP): Double {

    val bd = BigDecimal.valueOf(this).setScale(decimalPlace, roundingMode)
    return bd.toDouble()
}

fun Double.roundToCeil(): Int = ceil(this).roundToInt()

fun Double.roundToFloor(): Int = floor(this).roundToInt()

private const val defaultDoubleTolerance = 0.00001

fun Double.isEqualToWithTolerance(other: Double, tolerance: Double = defaultDoubleTolerance): Boolean = abs(this - other) <= tolerance