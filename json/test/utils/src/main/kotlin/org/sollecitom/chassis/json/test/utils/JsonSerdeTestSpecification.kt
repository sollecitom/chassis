package org.sollecitom.chassis.json.test.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.json.utils.serde.JsonSerde

interface JsonSerdeTestSpecification<VALUE : Any> {

    fun values(): List<VALUE>
    val jsonSerde: JsonSerde.SchemaAware<VALUE>

    @Test
    fun `serializing and deserializing to and from JSON`() {

        values().forEach { value ->

            val json = jsonSerde.serialize(value)
            val deserialized = jsonSerde.deserialize(json)

            assertThat(deserialized).isEqualTo(value)
            assertThat(json).compliesWith(jsonSerde.schema)
        }
    }
}