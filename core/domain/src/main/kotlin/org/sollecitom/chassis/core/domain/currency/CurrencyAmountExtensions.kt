package org.sollecitom.chassis.core.domain.currency

fun <CURRENCY_AMOUNT : CurrencyAmount<CURRENCY_AMOUNT>> CURRENCY_AMOUNT.times(value: Long): CURRENCY_AMOUNT = times(value.toBigInteger())

fun <CURRENCY_AMOUNT : CurrencyAmount<CURRENCY_AMOUNT>> CURRENCY_AMOUNT.div(value: Long): CURRENCY_AMOUNT = div(value.toBigInteger())

fun <CURRENCY_AMOUNT : CurrencyAmount<CURRENCY_AMOUNT>> CURRENCY_AMOUNT.divAndRemainder(value: Long): Pair<CURRENCY_AMOUNT, CURRENCY_AMOUNT> = divAndRemainder(value.toBigInteger())

fun <CURRENCY_AMOUNT : CurrencyAmount<CURRENCY_AMOUNT>> CURRENCY_AMOUNT.times(value: Int): CURRENCY_AMOUNT = times(value.toBigInteger())

fun <CURRENCY_AMOUNT : CurrencyAmount<CURRENCY_AMOUNT>> CURRENCY_AMOUNT.div(value: Int): CURRENCY_AMOUNT = div(value.toBigInteger())

fun <CURRENCY_AMOUNT : CurrencyAmount<CURRENCY_AMOUNT>> CURRENCY_AMOUNT.divAndRemainder(value: Int): Pair<CURRENCY_AMOUNT, CURRENCY_AMOUNT> = divAndRemainder(value.toBigInteger())