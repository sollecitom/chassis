package com.element.dpg.libs.chassis.core.test.utils

import assertk.Assert
import assertk.assertions.isNotZero
import assertk.assertions.isZero
import com.element.dpg.libs.chassis.core.domain.currency.SpecificCurrencyAmount

fun <CURRENCY : SpecificCurrencyAmount<CURRENCY>> Assert<CURRENCY>.isZero() = given { amount ->

    assertThat(amount.units).isZero()
}

fun <CURRENCY : SpecificCurrencyAmount<CURRENCY>> Assert<CURRENCY>.isNotZero() = given { amount ->

    assertThat(amount.units).isNotZero()
}