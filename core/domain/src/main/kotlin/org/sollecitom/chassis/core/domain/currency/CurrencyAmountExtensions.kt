package org.sollecitom.chassis.core.domain.currency

import org.sollecitom.chassis.kotlin.extensions.number.withPrecision
import org.sollecitom.chassis.kotlin.extensions.text.indexOfOrNull
import java.math.BigDecimal

@Suppress("UNCHECKED_CAST")
fun <CURRENCY_AMOUNT : CurrencyAmount> CURRENCY_AMOUNT.times(value: Long): CURRENCY_AMOUNT = times(value.toBigInteger()) as CURRENCY_AMOUNT

@Suppress("UNCHECKED_CAST")
fun <CURRENCY_AMOUNT : CurrencyAmount> CURRENCY_AMOUNT.div(value: Long): CURRENCY_AMOUNT = div(value.toBigInteger()) as CURRENCY_AMOUNT

@Suppress("UNCHECKED_CAST")
fun <CURRENCY_AMOUNT : CurrencyAmount> CURRENCY_AMOUNT.divAndRemainder(value: Long): Pair<CURRENCY_AMOUNT, CURRENCY_AMOUNT> = divAndRemainder(value.toBigInteger()) as Pair<CURRENCY_AMOUNT, CURRENCY_AMOUNT>

@Suppress("UNCHECKED_CAST")
fun <CURRENCY_AMOUNT : CurrencyAmount> CURRENCY_AMOUNT.times(value: Int): CURRENCY_AMOUNT = times(value.toBigInteger()) as CURRENCY_AMOUNT

@Suppress("UNCHECKED_CAST")
fun <CURRENCY_AMOUNT : CurrencyAmount> CURRENCY_AMOUNT.div(value: Int): CURRENCY_AMOUNT = div(value.toBigInteger()) as CURRENCY_AMOUNT

@Suppress("UNCHECKED_CAST")
fun <CURRENCY_AMOUNT : CurrencyAmount> CURRENCY_AMOUNT.divAndRemainder(value: Int): Pair<CURRENCY_AMOUNT, CURRENCY_AMOUNT> = divAndRemainder(value.toBigInteger()) as Pair<CURRENCY_AMOUNT, CURRENCY_AMOUNT>

operator fun <CURRENCY_AMOUNT : CurrencyAmount> CURRENCY_AMOUNT.times(value: Double) = withNewValue(units.toBigDecimal().movePointLeft(currency.fractionalDigits.value) * value.toBigDecimal())

operator fun <CURRENCY_AMOUNT : CurrencyAmount> CURRENCY_AMOUNT.div(value: Double) = withNewValue(units.toBigDecimal().movePointLeft(currency.fractionalDigits.value) / value.toBigDecimal())

@Suppress("UNCHECKED_CAST")
fun <CURRENCY_AMOUNT : CurrencyAmount> CURRENCY_AMOUNT.withNewValue(value: BigDecimal): CURRENCY_AMOUNT {

    val newValueRounded = value.withPrecision(currency.fractionalDigits.value)
    val newUnits = newValueRounded.toUnits(currency)
    return withUnits(newUnits) as CURRENCY_AMOUNT
}