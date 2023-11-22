package org.sollecitom.chassis.kotlin.extensions.number

import org.sollecitom.chassis.kotlin.extensions.text.indexOfOrNull
import java.math.BigDecimal

fun BigDecimal.withPrecision(precision: Int): BigDecimal {
    val plain = movePointRight(precision).toPlainString()
    return BigDecimal(plain.substring(0, plain.indexOfOrNull(".") ?: plain.length)).movePointLeft(precision)
}