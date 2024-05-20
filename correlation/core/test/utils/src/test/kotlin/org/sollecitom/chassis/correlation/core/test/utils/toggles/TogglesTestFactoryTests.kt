package org.sollecitom.chassis.correlation.core.test.utils.toggles

import assertk.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.toggles.BooleanToggleValue
import org.sollecitom.chassis.correlation.core.domain.toggles.DecimalToggleValue
import org.sollecitom.chassis.correlation.core.domain.toggles.EnumToggleValue
import org.sollecitom.chassis.correlation.core.domain.toggles.Toggles
import org.sollecitom.chassis.test.utils.assertions.containsSameElementsAs

@TestInstance(PER_CLASS)
private class TogglesTestFactoryTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `with given values`() {

        val toggle1 = BooleanToggleValue(id = newId.internal(), value = true)
        val toggle2 = DecimalToggleValue(id = newId.internal(), value = 12.5)
        val toggle3 = EnumToggleValue(id = newId.internal(), value = "HIGH")
        val values = setOf(toggle1, toggle2, toggle3)

        val toggles = Toggles.create(values = values)

        assertThat(toggles.values).containsSameElementsAs(values)
    }
}