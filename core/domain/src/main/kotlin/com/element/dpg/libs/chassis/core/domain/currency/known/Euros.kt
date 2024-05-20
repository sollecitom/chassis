package com.element.dpg.libs.chassis.core.domain.currency.known

import com.element.dpg.libs.chassis.core.domain.currency.JavaCurrencyAdapter
import com.element.dpg.libs.chassis.core.domain.currency.SpecificCurrencyAmountTemplate
import com.element.dpg.libs.chassis.core.domain.currency.toUnits
import java.math.BigDecimal
import java.math.BigInteger

private val eur: com.element.dpg.libs.chassis.core.domain.currency.Currency<Euros> by lazy { JavaCurrencyAdapter(java.util.Currency.getInstance("EUR")) }
val com.element.dpg.libs.chassis.core.domain.currency.Currency.Companion.EUR get() = eur

class Euros(units: BigInteger) : SpecificCurrencyAmountTemplate<Euros>(units, com.element.dpg.libs.chassis.core.domain.currency.Currency.EUR, ::Euros) {

    constructor(decimalValue: BigDecimal) : this(decimalValue.toUnits(com.element.dpg.libs.chassis.core.domain.currency.Currency.EUR))
}

val Number.euros: Euros get() = Euros(toDouble().toBigDecimal())
val Int.euroCents: Euros get() = Euros(toBigInteger())
val Long.euroCents: Euros get() = Euros(toBigInteger())