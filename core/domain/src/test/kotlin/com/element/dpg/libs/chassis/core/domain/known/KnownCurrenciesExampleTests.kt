package com.element.dpg.libs.chassis.core.domain.known

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import com.element.dpg.libs.chassis.core.domain.currency.div
import com.element.dpg.libs.chassis.core.domain.currency.known.cents
import com.element.dpg.libs.chassis.core.domain.currency.known.dollars
import com.element.dpg.libs.chassis.core.domain.currency.known.pounds
import com.element.dpg.libs.chassis.core.domain.currency.times
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class KnownCurrenciesExampleTests {

    @Test
    fun `using dollars`() {

        val amount = 5.dollars + 25.cents

        assertThat(amount.units).isEqualTo(525.toBigInteger())
        assertThat(amount.decimalValue).isEqualTo(5.25.toBigDecimal())
        assertThat(amount).isEqualTo(5.25.dollars)
    }

    @Test
    fun `attempting to create invalid currency amounts`() {

        val invalidGbpAmount = 1.001

        val result = runCatching { invalidGbpAmount.pounds }

        assertThat(result).isFailure().isInstanceOf<IllegalArgumentException>()
    }

    @Test
    fun `multiplying by a number that results in a correct number of decimal places`() {

        val amount = 12.4.dollars
        val factor = 2.5

        val result = amount * factor

        assertThat(result).isEqualTo(31.dollars)
    }

    @Test
    fun `dividing by a number that results in a correct number of decimal places`() {

        val amount = 31.dollars
        val factor = 2.5

        val result = amount / factor

        assertThat(result).isEqualTo(12.4.dollars)
    }

    @Test
    fun `multiplying a small amount by a number that results in a truncated number of decimal places`() {

        val factor = 0.09

        val result = 0.11.dollars * factor

        assertThat(result).isEqualTo(0.dollars)
    }

    @Test
    fun `multiplying zero by a number that results in a truncated number of decimal places`() {

        val factor = 0.9983218

        val result = 0.dollars * factor

        assertThat(result).isEqualTo(0.dollars)
    }

    @Test
    fun `multiplying by a number that results in a truncated number of decimal places`() {

        val amount = 11.32.dollars
        val factor = 2.67

        val result = amount * factor

        assertThat(result).isEqualTo(30.22.dollars)
    }

    @Test
    fun `dividing by a number that results in a truncated number of decimal places`() {

        val amount = 30.22.dollars
        val factor = 2.67

        val result = amount / factor

        assertThat(result).isEqualTo(11.32.dollars)
    }
}