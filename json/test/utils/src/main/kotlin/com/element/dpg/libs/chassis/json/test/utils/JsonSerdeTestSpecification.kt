package com.element.dpg.libs.chassis.json.test.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerde
import com.element.dpg.libs.chassis.test.utils.params.ParameterizedTestSupport
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
interface JsonSerdeTestSpecification<VALUE : Any> {

    val jsonSerde: JsonSerde.SchemaAware<VALUE>

    fun parameterizedArguments(): List<Pair<String, VALUE>>

    fun arguments(): Stream<Arguments> = ParameterizedTestSupport.arguments(*parameterizedArguments().toTypedArray())

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