package org.sollecitom.chassis.openapi.checking.checker.rules

import io.swagger.v3.oas.models.OpenAPI
import org.sollecitom.chassis.kotlin.extensions.text.capitalized
import org.sollecitom.chassis.openapi.checking.checker.model.ParameterLocation
import org.sollecitom.chassis.openapi.checking.checker.model.ParameterWithLocation
import org.sollecitom.chassis.openapi.checking.checker.model.allParameters
import org.sollecitom.chassis.openapi.checking.checker.rule.OpenApiRule
import org.sollecitom.chassis.openapi.checking.checker.rule.RuleResult

class WhitelistedAlphabetParameterNameRule(val pathAlphabet: Set<Char>, val headerAlphabet: Set<Char> = pathAlphabet, val queryAlphabet: Set<Char> = pathAlphabet, val cookieAlphabet: Set<Char> = pathAlphabet) : OpenApiRule {

    override fun check(api: OpenAPI): RuleResult {

        val violations = api.allParameters().asSequence().mapNotNull { parameter -> check(parameter) }.toSet()
        return RuleResult.withViolations(violations)
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

    data class Violation(val parameter: ParameterWithLocation, val whitelistedAlphabet: Set<Char>) : RuleResult.Violation {

        override val message = "${parameter.location.value.capitalized()} parameter with name ${parameter.parameter.name} for operation ${parameter.location.operation.method} on path ${parameter.location.pathName} should only contain characters in $whitelistedAlphabet but doesn't"
    }
}