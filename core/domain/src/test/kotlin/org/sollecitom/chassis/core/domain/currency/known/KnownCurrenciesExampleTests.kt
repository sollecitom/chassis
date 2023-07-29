package org.sollecitom.chassis.core.domain.currency.known

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
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
}