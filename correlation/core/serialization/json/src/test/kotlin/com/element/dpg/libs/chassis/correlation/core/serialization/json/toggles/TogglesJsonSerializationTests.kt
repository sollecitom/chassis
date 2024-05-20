package com.element.dpg.libs.chassis.correlation.core.serialization.json.toggles

import com.element.dpg.libs.chassis.core.test.utils.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.toggles.ToggleValue
import com.element.dpg.libs.chassis.correlation.core.domain.toggles.Toggles
import com.element.dpg.libs.chassis.correlation.core.test.utils.toggles.*
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class SessionJsonSerializationTests : JsonSerdeTestSpecification<Toggles>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Toggles.jsonSerde

    override fun parameterizedArguments() = listOf(
        "empty" to Toggles.create(),
        "not-empty" to notEmptyToggles(),
    )

    private fun notEmptyToggles(): Toggles {

        val boolean = ToggleValue.boolean(value = false)
        val integer = ToggleValue.integer(value = 10)
        val decimal = ToggleValue.decimal(value = -7.83)
        val enum = ToggleValue.enum(Amazing.YES)
        return Toggles(values = setOf(boolean, integer, decimal, enum))
    }

    private enum class Amazing {
        NO, YES
    }
}