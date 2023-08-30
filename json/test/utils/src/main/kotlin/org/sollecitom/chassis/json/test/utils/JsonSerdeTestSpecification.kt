package org.sollecitom.chassis.json.test.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.test.utils.params.ParameterizedTestSupport
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
interface JsonSerdeTestSpecification<VALUE : Any> {

    val jsonSerde: JsonSerde.SchemaAware<VALUE>

    fun arguments(): Stream<Arguments> // TODO try to remove this, and construct it instead from a typed one

    @ParameterizedTest
    @MethodSource("arguments")
    fun `serializing and deserializing to and from JSON`(argument: ParameterizedTestSupport.InlineWrapper<VALUE>) {

        val value = argument.value
        val json = jsonSerde.serialize(value)
        val deserialized = jsonSerde.deserialize(json)

        assertThat(deserialized).isEqualTo(value)
        assertThat(json).compliesWith(jsonSerde.schema)
    }
}