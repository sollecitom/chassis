package org.sollecitom.chassis.core.domain.currency

import java.math.BigDecimal
import java.math.BigInteger

interface CurrencyAmount {

    val units: BigInteger
    val currency: Currency<*>
    val decimalValue: BigDecimal

    fun withUnits(units: BigInteger): CurrencyAmount

    fun plusUnits(units: BigInteger): CurrencyAmount

    fun minusUnits(units: BigInteger): CurrencyAmount

    operator fun times(value: BigInteger): CurrencyAmount

    operator fun div(value: BigInteger): CurrencyAmount

    fun divAndRemainder(value: BigInteger): Pair<CurrencyAmount, CurrencyAmount>
}