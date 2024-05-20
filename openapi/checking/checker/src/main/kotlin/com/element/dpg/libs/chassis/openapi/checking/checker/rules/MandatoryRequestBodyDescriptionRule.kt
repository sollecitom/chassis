package com.element.dpg.libs.chassis.openapi.checking.checker.rules

import com.element.dpg.libs.chassis.openapi.checking.checker.model.OperationWithContext
import com.element.dpg.libs.chassis.openapi.checking.checker.model.allOperations
import com.element.dpg.libs.chassis.openapi.checking.checker.rule.OpenApiRule
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem.HttpMethod
import io.swagger.v3.oas.models.parameters.RequestBody


class MandatoryRequestBodyDescriptionRule(private val methods: Set<HttpMethod>) : OpenApiRule {

    init {
        require(methods.isNotEmpty()) { "Must specify at least one method to check" }
    }

    override fun invoke(api: OpenAPI): OpenApiRule.Result {

        val violations = api.allOperations().asSequence().filter { it.operation.method in methods }.mapNotNull { operation -> check(operation) }.toSet()
        return OpenApiRule.Result.withViolations(violations)
    }

    private fun check(operation: OperationWithContext): Violation? {

        if (operation.isNotCompliant()) return operation.violation()
        return null
    }

    private fun OperationWithContext.violation() = Violation(this, requestBody!!)

    private fun OperationWithContext.isNotCompliant(): Boolean = requestBody != null && requestBody.description.isNullOrBlank()

    data class Violation(val operation: OperationWithContext, val requestBody: RequestBody) : OpenApiRule.Result.Violation {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} should specify a non-blank description for the request body, but doesn't"
    }
}