package org.sollecitom.chassis.core.domain.currency.known

import org.sollecitom.chassis.core.domain.currency.Currency
import org.sollecitom.chassis.core.domain.currency.JavaCurrencyAdapter
import org.sollecitom.chassis.core.domain.currency.SpecificCurrencyAmountTemplate
import java.math.BigDecimal
import java.math.BigInteger

private val jpy: Currency by lazy { JavaCurrencyAdapter(java.util.Currency.getInstance("JPY")) }
val Currency.Companion.JPY get() = jpy

class Yen(units: BigInteger) : SpecificCurrencyAmountTemplate<Yen>(units, Currency.JPY, ::Yen) {

    constructor(decimalValue: BigDecimal) : this(decimalValue.movePointRight(Currency.JPY.fractionalDigits.value).toBigInteger())
}

val Int.yen: Yen get() = Yen(toBigInteger())
val Long.yen: Yen get() = Yen(toBigInteger())