package com.element.dpg.libs.chassis.core.domain.currency

import java.math.BigInteger

interface SpecificCurrencyAmount<SELF : SpecificCurrencyAmount<SELF>> : com.element.dpg.libs.chassis.core.domain.currency.CurrencyAmount {

    override val currency: com.element.dpg.libs.chassis.core.domain.currency.Currency<SELF>

    operator fun plus(other: SELF): SELF

    operator fun minus(other: SELF): SELF

    override fun withUnits(units: BigInteger): SELF

    override fun plusUnits(units: BigInteger): SELF

    override fun minusUnits(units: BigInteger): SELF

    override operator fun times(value: BigInteger): SELF

    override operator fun div(value: BigInteger): SELF

    override fun divAndRemainder(value: BigInteger): Pair<SELF, SELF>
}