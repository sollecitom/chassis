package org.sollecitom.chassis.core.domain.currency.known

import org.sollecitom.chassis.core.domain.currency.Currency
import org.sollecitom.chassis.core.domain.currency.JavaCurrencyAdapter
import org.sollecitom.chassis.core.domain.currency.SpecificCurrencyAmountTemplate
import java.math.BigDecimal
import java.math.BigInteger

private val gbp: Currency by lazy { JavaCurrencyAdapter(java.util.Currency.getInstance("GBP")) }
val Currency.Companion.GBP get() = gbp

class Pounds(units: BigInteger) : SpecificCurrencyAmountTemplate<Pounds>(units, Currency.GBP, ::Pounds) {

    constructor(decimalValue: BigDecimal) : this(decimalValue.movePointRight(Currency.GBP.fractionalDigits.value).toBigInteger())
}

val Number.pounds: Pounds get() = Pounds(toDouble().toBigDecimal())
val Int.pence: Pounds get() = Pounds(toBigInteger())
val Long.pence: Pounds get() = Pounds(toBigInteger())