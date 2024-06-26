package com.element.dpg.libs.chassis.core.domain.currency

import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.domain.quantity.Count
import java.util.*

interface Currency<AMOUNT : SpecificCurrencyAmount<AMOUNT>> {

    val textualCode: Name
    val numericCode: Name
    val fractionalDigits: Count

    fun displayName(locale: Locale = Locale.getDefault(Locale.Category.DISPLAY)): Name

    fun symbol(locale: Locale = Locale.getDefault(Locale.Category.DISPLAY)): Name

    companion object
}