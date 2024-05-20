package com.element.dpg.libs.chassis.core.domain.currency.known

import com.element.dpg.libs.chassis.core.domain.currency.Currency
import org.sollecitom.chassis.core.domain.currency.JavaCurrencyAdapter
import org.sollecitom.chassis.core.domain.currency.SpecificCurrencyAmountTemplate
import org.sollecitom.chassis.core.domain.currency.toUnits
import java.math.BigDecimal
import java.math.BigInteger

private val usd: com.element.dpg.libs.chassis.core.domain.currency.Currency<Dollars> by lazy { JavaCurrencyAdapter(java.util.Currency.getInstance("USD")) }
val com.element.dpg.libs.chassis.core.domain.currency.Currency.Companion.USD get() = usd

class Dollars(units: BigInteger) : SpecificCurrencyAmountTemplate<Dollars>(units, com.element.dpg.libs.chassis.core.domain.currency.Currency.USD, ::Dollars) {

    constructor(decimalValue: BigDecimal) : this(decimalValue.toUnits(com.element.dpg.libs.chassis.core.domain.currency.Currency.USD))
}

val Number.dollars: Dollars get() = Dollars(toDouble().toBigDecimal())
val Int.cents: Dollars get() = Dollars(toBigInteger())
val Long.cents: Dollars get() = Dollars(toBigInteger())