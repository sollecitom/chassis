package com.element.dpg.libs.chassis.test.utils.params

import org.junit.jupiter.api.Named
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream

object ParameterizedTestSupport {

    fun <VALUE : Any> arguments(vararg args: Pair<String, VALUE>): Stream<Arguments> = args.asList().stream().map { (argName, arg) -> Arguments.of(Named.of(argName, InlineWrapper(arg))) }

    data class InlineWrapper<VALUE : Any>(val value: VALUE) // to work around inline classes not working with junit-5 params
}