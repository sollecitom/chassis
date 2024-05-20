package com.element.dpg.libs.chassis.core.domain.currency

import java.math.BigDecimal

@JvmInline
value class CurrencyConversionRate(val value: BigDecimal) : Comparable<CurrencyConversionRate> {

    init {
        require(value > BigDecimal.ZERO) { "A currency conversion rate should be greater than 0" }
    }

    override fun compareTo(other: CurrencyConversionRate) = value.compareTo(other.value)
}