package org.sollecitom.chassis.correlation.core.serialization.json.access.authorization

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.authorization.Roles
import org.sollecitom.chassis.correlation.core.serialization.json.access.autorization.jsonSerde
import org.sollecitom.chassis.correlation.core.test.utils.access.authorization.TestRoles
import org.sollecitom.chassis.json.test.utils.compliesWith
import org.sollecitom.chassis.json.utils.serde.JsonSerde

@TestInstance(PER_CLASS)
private class RolesJsonSerializationTests : JsonSerdeTestSpecification<Roles>, WithCoreGenerators by WithCoreGenerators.testProvider {

    override fun value() = TestRoles.default

    override val jsonSerde get() = Roles.jsonSerde
}

interface JsonSerdeTestSpecification<VALUE : Any> {

    fun value(): VALUE
    val jsonSerde: JsonSerde.SchemaAware<VALUE>

    @Test
    fun `serializing and deserializing to and from JSON`() {

        val value = value()

        val json = jsonSerde.serialize(value)
        val deserialized = jsonSerde.deserialize(json)

        assertThat(deserialized).isEqualTo(value)
        assertThat(json).compliesWith(Roles.jsonSerde.schema)
    }
}