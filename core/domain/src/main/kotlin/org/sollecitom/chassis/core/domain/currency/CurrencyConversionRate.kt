package org.sollecitom.chassis.core.domain.currency

import java.math.BigDecimal

@JvmInline
value class CurrencyConversionRate(val value: BigDecimal) : Comparable<CurrencyConversionRate> {

    override fun compareTo(other: CurrencyConversionRate) = value.compareTo(other.value)
}