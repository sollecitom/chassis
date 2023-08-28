package org.sollecitom.chassis.correlation.core.serialization.json

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.origin.Origin
import org.sollecitom.chassis.correlation.core.test.utils.origin.create
import org.sollecitom.chassis.json.test.utils.compliesWith

@TestInstance(PER_CLASS)
private class OriginJsonSerializationTests : WithCoreGenerators by WithCoreGenerators.testProvider {

    @Test
    fun `serializing and deserializing to and from JSON`() {

        val origin = Origin.create()

        val json = Origin.jsonSerde.serialize(origin)
        val deserialized = Origin.jsonSerde.deserialize(json)

        assertThat(deserialized).isEqualTo(origin)
        assertThat(json).compliesWith(Origin.jsonSerde.schema)
    }
}
