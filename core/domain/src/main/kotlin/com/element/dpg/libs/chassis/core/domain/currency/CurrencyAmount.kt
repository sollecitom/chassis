package com.element.dpg.libs.chassis.core.domain.currency

import java.math.BigDecimal
import java.math.BigInteger

interface CurrencyAmount {

    val units: BigInteger
    val currency: com.element.dpg.libs.chassis.core.domain.currency.Currency<*>
    val decimalValue: BigDecimal

    fun withUnits(units: BigInteger): com.element.dpg.libs.chassis.core.domain.currency.CurrencyAmount

    fun plusUnits(units: BigInteger): com.element.dpg.libs.chassis.core.domain.currency.CurrencyAmount

    fun minusUnits(units: BigInteger): com.element.dpg.libs.chassis.core.domain.currency.CurrencyAmount

    operator fun times(value: BigInteger): com.element.dpg.libs.chassis.core.domain.currency.CurrencyAmount

    operator fun div(value: BigInteger): com.element.dpg.libs.chassis.core.domain.currency.CurrencyAmount

    fun divAndRemainder(value: BigInteger): Pair<com.element.dpg.libs.chassis.core.domain.currency.CurrencyAmount, com.element.dpg.libs.chassis.core.domain.currency.CurrencyAmount>
}