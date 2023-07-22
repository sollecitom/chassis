package org.sollecitom.chassis.core.domain.currency

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.quantity.Count
import java.util.*
import java.util.Currency as JavaCurrency

@JvmInline
internal value class JavaCurrencyAdapter(private val currency: JavaCurrency) : Currency {

    override val textualCode get() = currency.currencyCode.let(::Name)
    override val numericCode get() = currency.numericCodeAsString.let(::Name)

    override val fractionalDigits: Count get() = currency.defaultFractionDigits.let(::Count)

    override fun displayName(locale: Locale) = currency.getDisplayName(locale).let(::Name)

    override fun symbol(locale: Locale) = currency.getSymbol(locale).let(::Name)
}