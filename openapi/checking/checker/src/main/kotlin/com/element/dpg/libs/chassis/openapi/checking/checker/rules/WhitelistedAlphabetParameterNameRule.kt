package com.element.dpg.libs.chassis.openapi.checking.checker.rules

import io.swagger.v3.oas.models.OpenAPI
import com.element.dpg.libs.chassis.kotlin.extensions.text.capitalized
import com.element.dpg.libs.chassis.openapi.checking.checker.model.ParameterLocation
import com.element.dpg.libs.chassis.openapi.checking.checker.model.ParameterWithLocation
import com.element.dpg.libs.chassis.openapi.checking.checker.model.allParameters
import com.element.dpg.libs.chassis.openapi.checking.checker.rule.OpenApiRule


class WhitelistedAlphabetParameterNameRule(val pathAlphabet: Set<Char>, val headerAlphabet: Set<Char> = pathAlphabet, val queryAlphabet: Set<Char> = pathAlphabet, val cookieAlphabet: Set<Char> = pathAlphabet) : OpenApiRule {

    override fun invoke(api: OpenAPI): OpenApiRule.Result {

        val violations = api.allParameters().asSequence().mapNotNull { parameter -> check(parameter) }.toSet()
        return OpenApiRule.Result.withViolations(violations)
    }

    private fun check(parameter: ParameterWithLocation): Violation? {

        if (parameter.isNotCompliantWithNamingConvention()) return parameter.violation()
        return null
    }

    private val ParameterWithLocation.whitelistedAlphabet: Set<Char>
        get() = when (location) {
            is ParameterLocation.Path -> pathAlphabet
            is ParameterLocation.Cookie -> cookieAlphabet
            is ParameterLocation.Header -> headerAlphabet
            is ParameterLocation.Query -> queryAlphabet
        }

    private fun ParameterWithLocation.violation() = Violation(this, whitelistedAlphabet)

    private fun ParameterWithLocation.isNotCompliantWithNamingConvention(): Boolean = parameter.name.any { character -> character !in whitelistedAlphabet }

    data class Violation(val parameter: ParameterWithLocation, val whitelistedAlphabet: Set<Char>) : OpenApiRule.Result.Violation {

        override val message = "${parameter.location.value.capitalized()} parameter with name ${parameter.parameter.name} for operation ${parameter.location.operation.method} on path ${parameter.location.pathName} should only contain characters in $whitelistedAlphabet but doesn't"
    }
}