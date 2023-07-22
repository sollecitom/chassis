package org.sollecitom.chassis.core.domain.currency.known

import org.sollecitom.chassis.core.domain.currency.Currency
import org.sollecitom.chassis.core.domain.currency.JavaCurrencyAdapter
import org.sollecitom.chassis.core.domain.currency.SpecificCurrencyAmountTemplate
import java.math.BigDecimal
import java.math.BigInteger

private val usd: Currency by lazy { JavaCurrencyAdapter(java.util.Currency.getInstance("USD")) }
val Currency.Companion.USD get() = usd

class Dollars(units: BigInteger) : SpecificCurrencyAmountTemplate<Dollars>(units, Currency.USD, ::Dollars) {

    constructor(decimalValue: BigDecimal) : this(decimalValue.movePointRight(Currency.USD.fractionalDigits.value).toBigInteger())
}

val Number.dollars: Dollars get() = Dollars(toDouble().toBigDecimal())
val Int.cents: Dollars get() = Dollars(toBigInteger())
val Long.cents: Dollars get() = Dollars(toBigInteger())