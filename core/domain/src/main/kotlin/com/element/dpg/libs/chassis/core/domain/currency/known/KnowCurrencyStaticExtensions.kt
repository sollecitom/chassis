@file:Suppress("UNCHECKED_CAST")

package com.element.dpg.libs.chassis.core.domain.currency.known

import com.element.dpg.libs.chassis.core.domain.currency.SpecificCurrencyAmount
import com.element.dpg.libs.chassis.kotlin.extensions.number.withPrecision
import java.math.BigDecimal
import kotlin.reflect.KClass

val <CURRENCY : SpecificCurrencyAmount<CURRENCY>> KClass<CURRENCY>.currency: com.element.dpg.libs.chassis.core.domain.currency.Currency<CURRENCY>
    get() = when (this) {
        Dollars::class -> com.element.dpg.libs.chassis.core.domain.currency.Currency.USD as com.element.dpg.libs.chassis.core.domain.currency.Currency<CURRENCY>
        Euros::class -> com.element.dpg.libs.chassis.core.domain.currency.Currency.EUR as com.element.dpg.libs.chassis.core.domain.currency.Currency<CURRENCY>
        Pounds::class -> com.element.dpg.libs.chassis.core.domain.currency.Currency.GBP as com.element.dpg.libs.chassis.core.domain.currency.Currency<CURRENCY>
        Yen::class -> com.element.dpg.libs.chassis.core.domain.currency.Currency.JPY as com.element.dpg.libs.chassis.core.domain.currency.Currency<CURRENCY>
        else -> error("Unknown currency for currency amount class $this")
    }

fun <CURRENCY : SpecificCurrencyAmount<CURRENCY>> BigDecimal.toCurrencyAmount(currency: com.element.dpg.libs.chassis.core.domain.currency.Currency<CURRENCY>): CURRENCY {

    val rounded = withPrecision(currency.fractionalDigits.value)
    return when (currency) {
        com.element.dpg.libs.chassis.core.domain.currency.Currency.USD -> Dollars(rounded) as CURRENCY
        com.element.dpg.libs.chassis.core.domain.currency.Currency.EUR -> Euros(rounded) as CURRENCY
        com.element.dpg.libs.chassis.core.domain.currency.Currency.GBP -> Pounds(rounded) as CURRENCY
        com.element.dpg.libs.chassis.core.domain.currency.Currency.JPY -> Yen(rounded) as CURRENCY
        else -> error("Unsupported currency $currency")
    }
}