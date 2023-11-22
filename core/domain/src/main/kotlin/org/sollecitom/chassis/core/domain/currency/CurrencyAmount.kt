package org.sollecitom.chassis.core.domain.currency

import java.math.BigDecimal
import java.math.BigInteger

interface CurrencyAmount<SELF : CurrencyAmount<SELF>> {

    val units: BigInteger
    val currency: Currency
    val decimalValue: BigDecimal

    fun withUnits(units: BigInteger): SELF

    fun plusUnits(units: BigInteger): SELF

    fun minusUnits(units: BigInteger): SELF

    operator fun times(value: BigInteger): SELF

    operator fun div(value: BigInteger): SELF

    fun divAndRemainder(value: BigInteger): Pair<SELF, SELF>
}