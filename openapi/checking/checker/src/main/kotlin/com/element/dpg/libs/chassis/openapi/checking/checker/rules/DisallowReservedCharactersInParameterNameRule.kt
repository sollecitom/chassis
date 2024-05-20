package com.element.dpg.libs.chassis.openapi.checking.checker.rules

import com.element.dpg.libs.chassis.kotlin.extensions.text.capitalized
import com.element.dpg.libs.chassis.openapi.checking.checker.model.ParameterWithLocation
import com.element.dpg.libs.chassis.openapi.checking.checker.model.allParameters
import com.element.dpg.libs.chassis.openapi.checking.checker.rule.OpenApiRule
import io.swagger.v3.oas.models.OpenAPI

object DisallowReservedCharactersInParameterNameRule : OpenApiRule {

    override fun invoke(api: OpenAPI): OpenApiRule.Result {

        val violations = api.allParameters().asSequence().mapNotNull { parameter -> check(parameter) }.toSet()
        return OpenApiRule.Result.withViolations(violations)
    }

    private fun check(parameter: ParameterWithLocation): Violation? {

        if (parameter.isNotCompliant()) return parameter.violation()
        return null
    }

    private fun ParameterWithLocation.violation() = Violation(this)

    private fun ParameterWithLocation.isNotCompliant(): Boolean = (parameter.allowReserved ?: false)

    data class Violation(val parameter: ParameterWithLocation) : OpenApiRule.Result.Violation {

        override val message = "${parameter.location.value.capitalized()} parameter with name ${parameter.parameter.name} for operation ${parameter.location.operation.method} on path ${parameter.location.pathName} shouldn't allow reserved URL characters but does"
    }
}