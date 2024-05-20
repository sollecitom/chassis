package com.element.dpg.libs.chassis.core.domain.currency

import java.math.BigDecimal
import java.math.BigInteger

data class GenericCurrencyAmount(override val units: BigInteger, override val currency: com.element.dpg.libs.chassis.core.domain.currency.Currency<*>) : com.element.dpg.libs.chassis.core.domain.currency.CurrencyAmount {

    init {
        require(units >= BigInteger.ZERO) { "Units cannot be less than zero" }
    }

    override val decimalValue: BigDecimal by lazy { units.toBigDecimal().movePointLeft(currency.fractionalDigits.value) }

    override fun withUnits(units: BigInteger) = GenericCurrencyAmount(units, currency)

    override fun plusUnits(units: BigInteger) = withUnits(this.units + units)

    override fun minusUnits(units: BigInteger) = withUnits(this.units - units)

    override fun times(value: BigInteger) = withUnits(units * value)

    override fun div(value: BigInteger) = withUnits(units / value)

    override fun divAndRemainder(value: BigInteger) = units.divideAndRemainder(value).let { GenericCurrencyAmount(it[0], currency) to GenericCurrencyAmount(it[1], currency) }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpecificCurrencyAmountTemplate<*>

        if (units != other.units) return false
        return currency == other.currency
    }

    override fun hashCode(): Int {
        var result = units.hashCode()
        result = 31 * result + currency.hashCode()
        return result
    }

    override fun toString() = "${currency.symbol().value}${String.format("%.${currency.fractionalDigits.value}f", decimalValue)}"
}