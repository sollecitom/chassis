package org.sollecitom.chassis.core.domain.currency

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.quantity.Count
import java.math.BigDecimal
import java.math.BigInteger
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

    companion object
}

private val usd: Currency by lazy { JavaCurrencyAdapter(JavaCurrency.getInstance("USD")) }
private val gbp: Currency by lazy { JavaCurrencyAdapter(JavaCurrency.getInstance("GBP")) }
private val eur: Currency by lazy { JavaCurrencyAdapter(JavaCurrency.getInstance("EUR")) }
private val jpy: Currency by lazy { JavaCurrencyAdapter(JavaCurrency.getInstance("JPY")) }

val Currency.Companion.USD get() = usd
val Currency.Companion.GBP get() = gbp
val Currency.Companion.EUR get() = eur
val Currency.Companion.JPY get() = jpy

interface CurrencyAmount<SELF : CurrencyAmount<SELF>> {

    val units: BigInteger
    val currency: Currency
    val decimalValue: BigDecimal

    operator fun times(value: BigInteger): SELF

    operator fun div(value: BigInteger): SELF

    fun divAndRemainder(value: BigInteger): Pair<SELF, SELF>
}

fun <CURRENCY_AMOUNT : CurrencyAmount<CURRENCY_AMOUNT>> CURRENCY_AMOUNT.times(value: Long): CURRENCY_AMOUNT = times(value.toBigInteger())

fun <CURRENCY_AMOUNT : CurrencyAmount<CURRENCY_AMOUNT>> CURRENCY_AMOUNT.div(value: Long): CURRENCY_AMOUNT = div(value.toBigInteger())

fun <CURRENCY_AMOUNT : CurrencyAmount<CURRENCY_AMOUNT>> CURRENCY_AMOUNT.divAndRemainder(value: Long): Pair<CURRENCY_AMOUNT, CURRENCY_AMOUNT> = divAndRemainder(value.toBigInteger())

fun <CURRENCY_AMOUNT : CurrencyAmount<CURRENCY_AMOUNT>> CURRENCY_AMOUNT.times(value: Int): CURRENCY_AMOUNT = times(value.toBigInteger())

fun <CURRENCY_AMOUNT : CurrencyAmount<CURRENCY_AMOUNT>> CURRENCY_AMOUNT.div(value: Int): CURRENCY_AMOUNT = div(value.toBigInteger())

fun <CURRENCY_AMOUNT : CurrencyAmount<CURRENCY_AMOUNT>> CURRENCY_AMOUNT.divAndRemainder(value: Int): Pair<CURRENCY_AMOUNT, CURRENCY_AMOUNT> = divAndRemainder(value.toBigInteger())

interface SpecificCurrencyAmount<SELF : SpecificCurrencyAmount<SELF>> : CurrencyAmount<SELF> {

    operator fun plus(other: SELF): SELF

    operator fun minus(other: SELF): SELF
}

abstract class SpecificCurrencyAmountTemplate<SELF : SpecificCurrencyAmountTemplate<SELF>>(final override val units: BigInteger, final override val currency: Currency, private val construct: (BigInteger) -> SELF) : SpecificCurrencyAmount<SELF> {

    init {
        require(units >= BigInteger.ZERO) { "Units cannot be less than zero" }
    }

    override val decimalValue: BigDecimal by lazy { units.toBigDecimal().movePointLeft(currency.fractionalDigits.value) }

    override fun plus(other: SELF) = construct(units + other.units)

    override fun minus(other: SELF) = construct(units - other.units)

    override fun times(value: BigInteger) = construct(units * value)

    override fun div(value: BigInteger) = construct(units / value)

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

class Dollars(units: BigInteger) : SpecificCurrencyAmountTemplate<Dollars>(units, Currency.USD, ::Dollars) {

    constructor(decimalValue: BigDecimal) : this((decimalValue * BigDecimal.TEN.pow(Currency.USD.fractionalDigits.value)).toBigInteger())
}

val Number.dollars: Dollars get() = Dollars(toDouble().toBigDecimal())
val Int.cents: Dollars get() = Dollars(toBigInteger())
val Long.cents: Dollars get() = Dollars(toBigInteger())

class Pounds(units: BigInteger) : SpecificCurrencyAmountTemplate<Pounds>(units, Currency.GBP, ::Pounds) {

    constructor(decimalValue: BigDecimal) : this((decimalValue * BigDecimal.TEN.pow(Currency.GBP.fractionalDigits.value)).toBigInteger())
}

val Number.pounds: Pounds get() = Pounds(toDouble().toBigDecimal())
val Int.pence: Pounds get() = Pounds(toBigInteger())
val Long.pence: Pounds get() = Pounds(toBigInteger())

class Euros(units: BigInteger) : SpecificCurrencyAmountTemplate<Euros>(units, Currency.EUR, ::Euros) {

    constructor(decimalValue: BigDecimal) : this((decimalValue * BigDecimal.TEN.pow(Currency.EUR.fractionalDigits.value)).toBigInteger())
}

val Number.euros: Euros get() = Euros(toDouble().toBigDecimal())
val Int.euroCents: Euros get() = Euros(toBigInteger())
val Long.euroCents: Euros get() = Euros(toBigInteger())

class Yen(units: BigInteger) : SpecificCurrencyAmountTemplate<Yen>(units, Currency.JPY, ::Yen) {

    constructor(decimalValue: BigDecimal) : this((decimalValue * BigDecimal.TEN.pow(Currency.JPY.fractionalDigits.value)).toBigInteger())
}

val Int.yen: Yen get() = Yen(toBigInteger())
val Long.yen: Yen get() = Yen(toBigInteger())

fun main() {

    val fiver = 5.pounds
    val twentyPence = 20.pence

    val total = fiver + twentyPence

    println(total)
}