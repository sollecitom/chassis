package org.sollecitom.chassis.core.domain.currency

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.quantity.Count
import java.util.*
import java.util.Currency as JavaCurrency

data class GenericCurrencyAmount(val units: Long, val currency: Currency)

//data class CurrencyData(val name: Name, val code: Name, val fractionalDigits: Count) {
//
//    constructor(currency: JavaCurrency) : this(currency)
//}

@JvmInline
value class JavaCurrencyAdapter(private val currency: JavaCurrency) : Currency {

    override val textualCode get() = currency.currencyCode.let(::Name)
    override val numericCode get() = currency.numericCodeAsString.let(::Name)

    override val fractionalDigits: Count get() = currency.defaultFractionDigits.let(::Count)

    override fun displayName(locale: Locale) = currency.getDisplayName(locale).let(::Name)

    override fun symbol(locale: Locale) = currency.getSymbol(locale).let(::Name)
}

interface Currency {

    val textualCode: Name
    val numericCode: Name
    val fractionalDigits: Count

    fun displayName(locale: Locale = Locale.getDefault(Locale.Category.DISPLAY)): Name

    fun symbol(locale: Locale = Locale.getDefault(Locale.Category.DISPLAY)): Name

    data class Code(val textual: Name, val numeric: Name)
}

interface CurrencyAmount {

    val units: Long
    val currency: Currency
}