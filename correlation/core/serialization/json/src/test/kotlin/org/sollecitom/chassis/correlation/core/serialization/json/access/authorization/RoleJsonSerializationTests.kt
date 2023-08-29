package org.sollecitom.chassis.correlation.core.serialization.json.access.authorization

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.authorization.Role
import org.sollecitom.chassis.correlation.core.serialization.json.access.autorization.jsonSerde
import org.sollecitom.chassis.json.test.utils.compliesWith

@TestInstance(PER_CLASS)
private class RoleJsonSerializationTests : WithCoreGenerators by WithCoreGenerators.testProvider {

    @Test
    fun `serializing and deserializing to and from JSON`() {

        val role = Role("some-role".let(::Name))

        val json = Role.jsonSerde.serialize(role)
        val deserialized = Role.jsonSerde.deserialize(json)

        assertThat(deserialized).isEqualTo(role)
        assertThat(json).compliesWith(Role.jsonSerde.schema)
    }
}