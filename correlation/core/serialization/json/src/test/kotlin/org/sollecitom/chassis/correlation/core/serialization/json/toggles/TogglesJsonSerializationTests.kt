package org.sollecitom.chassis.correlation.core.serialization.json.toggles

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.toggles.ToggleValue
import org.sollecitom.chassis.correlation.core.domain.toggles.Toggles
import org.sollecitom.chassis.correlation.core.test.utils.toggles.*
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class SessionJsonSerializationTests : JsonSerdeTestSpecification<Toggles>, WithCoreGenerators by WithCoreGenerators.testProvider {

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