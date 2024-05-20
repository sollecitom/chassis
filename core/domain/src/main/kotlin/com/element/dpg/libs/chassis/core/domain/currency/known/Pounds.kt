package com.element.dpg.libs.chassis.core.domain.currency.known

import com.element.dpg.libs.chassis.core.domain.currency.JavaCurrencyAdapter
import com.element.dpg.libs.chassis.core.domain.currency.SpecificCurrencyAmountTemplate
import com.element.dpg.libs.chassis.core.domain.currency.toUnits
import java.math.BigDecimal
import java.math.BigInteger

private val gbp: com.element.dpg.libs.chassis.core.domain.currency.Currency<Pounds> by lazy { JavaCurrencyAdapter(java.util.Currency.getInstance("GBP")) }
val com.element.dpg.libs.chassis.core.domain.currency.Currency.Companion.GBP get() = gbp

class Pounds(units: BigInteger) : SpecificCurrencyAmountTemplate<Pounds>(units, com.element.dpg.libs.chassis.core.domain.currency.Currency.GBP, ::Pounds) {

    constructor(decimalValue: BigDecimal) : this(decimalValue.toUnits(com.element.dpg.libs.chassis.core.domain.currency.Currency.GBP))
}

val Number.pounds: Pounds get() = Pounds(toDouble().toBigDecimal())
val Int.pence: Pounds get() = Pounds(toBigInteger())
val Long.pence: Pounds get() = Pounds(toBigInteger())