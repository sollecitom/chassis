package org.sollecitom.chassis.core.domain.currency.known

import org.sollecitom.chassis.core.domain.currency.Currency
import org.sollecitom.chassis.core.domain.currency.JavaCurrencyAdapter
import org.sollecitom.chassis.core.domain.currency.SpecificCurrencyAmountTemplate
import org.sollecitom.chassis.core.domain.currency.toUnits
import java.math.BigDecimal
import java.math.BigInteger

private val gbp: Currency by lazy { JavaCurrencyAdapter(java.util.Currency.getInstance("GBP")) }
val Currency.Companion.GBP get() = gbp

class Pounds(units: BigInteger) : SpecificCurrencyAmountTemplate<Pounds>(units, Currency.GBP, ::Pounds) {

    constructor(decimalValue: BigDecimal) : this(decimalValue.toUnits(Currency.GBP))
}

val Number.pounds: Pounds get() = Pounds(toDouble().toBigDecimal())
val Int.pence: Pounds get() = Pounds(toBigInteger())
val Long.pence: Pounds get() = Pounds(toBigInteger())