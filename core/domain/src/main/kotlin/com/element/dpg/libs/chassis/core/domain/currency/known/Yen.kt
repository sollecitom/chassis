package com.element.dpg.libs.chassis.core.domain.currency.known

import com.element.dpg.libs.chassis.core.domain.currency.Currency
import org.sollecitom.chassis.core.domain.currency.JavaCurrencyAdapter
import org.sollecitom.chassis.core.domain.currency.SpecificCurrencyAmountTemplate
import org.sollecitom.chassis.core.domain.currency.toUnits
import java.math.BigDecimal
import java.math.BigInteger

private val jpy: com.element.dpg.libs.chassis.core.domain.currency.Currency<Yen> by lazy { JavaCurrencyAdapter(java.util.Currency.getInstance("JPY")) }
val com.element.dpg.libs.chassis.core.domain.currency.Currency.Companion.JPY get() = jpy

class Yen(units: BigInteger) : SpecificCurrencyAmountTemplate<Yen>(units, com.element.dpg.libs.chassis.core.domain.currency.Currency.JPY, ::Yen) {

    constructor(decimalValue: BigDecimal) : this(decimalValue.toUnits(com.element.dpg.libs.chassis.core.domain.currency.Currency.JPY))
}

val Int.yen: Yen get() = Yen(toBigInteger())
val Long.yen: Yen get() = Yen(toBigInteger())