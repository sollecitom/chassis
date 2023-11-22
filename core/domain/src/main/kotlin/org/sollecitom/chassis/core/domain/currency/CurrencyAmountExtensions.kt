package org.sollecitom.chassis.core.domain.currency

import java.math.BigDecimal
import java.math.MathContext

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

    val newPrecision = value.precision() - value.scale() + currency.fractionalDigits.value
    val newValueRounded = value.round(MathContext(newPrecision))
    val newUnits = newValueRounded.toUnits(currency)
    return withUnits(newUnits) as CURRENCY_AMOUNT
}