@file:Suppress("UNCHECKED_CAST")

package org.sollecitom.chassis.core.domain.currency.known

import org.sollecitom.chassis.core.domain.currency.Currency
import org.sollecitom.chassis.core.domain.currency.SpecificCurrencyAmount
import kotlin.reflect.KClass

val <CURRENCY : SpecificCurrencyAmount<CURRENCY>> KClass<CURRENCY>.currency: Currency<CURRENCY>
    get() = when (this) {
        Dollars::class -> Currency.USD as Currency<CURRENCY>
        Euros::class -> Currency.EUR as Currency<CURRENCY>
        Pounds::class -> Currency.GBP as Currency<CURRENCY>
        Yen::class -> Currency.JPY as Currency<CURRENCY>
        else -> error("Unknown currency for currency amount class $this")
    }