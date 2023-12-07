package org.sollecitom.chassis.core.domain.currency

import java.math.BigDecimal
import java.math.BigInteger

abstract class SpecificCurrencyAmountTemplate<SELF : SpecificCurrencyAmountTemplate<SELF>>(final override val units: BigInteger, final override val currency: Currency<SELF>, private val construct: (BigInteger) -> SELF) : SpecificCurrencyAmount<SELF> {

    init {
        require(units >= BigInteger.ZERO) { "Units cannot be less than zero" }
    }

    override val decimalValue: BigDecimal by lazy { units.toBigDecimal().movePointLeft(currency.fractionalDigits.value) }

    override fun withUnits(units: BigInteger) = construct(units)

    override fun plusUnits(units: BigInteger) = withUnits(this.units + units)

    override fun minusUnits(units: BigInteger) =  withUnits(this.units - units)

    override fun plus(other: SELF) = plusUnits(other.units)

    override fun minus(other: SELF) = minusUnits(other.units)

    override fun times(value: BigInteger) = withUnits(units * value)

    override fun div(value: BigInteger) = withUnits(units / value)

    override fun divAndRemainder(value: BigInteger) = units.divideAndRemainder(value).let { construct(it[0]) to construct(it[1]) }

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpecificCurrencyAmountTemplate<*>

        if (units != other.units) return false
        return currency == other.currency
    }

    final override fun hashCode(): Int {
        var result = units.hashCode()
        result = 31 * result + currency.hashCode()
        return result
    }

    override fun toString() = "${currency.symbol().value}${String.format("%.${currency.fractionalDigits.value}f", decimalValue)}"
}

internal fun <AMOUNT : SpecificCurrencyAmount<AMOUNT>> BigDecimal.toUnits(currency: Currency<AMOUNT>): BigInteger = runCatching { movePointRight(currency.fractionalDigits.value).toBigIntegerExact() }.getOrElse { throw IllegalArgumentException(it) }