package com.element.dpg.libs.chassis.openapi.checking.checker.rules

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem.HttpMethod
import com.element.dpg.libs.chassis.openapi.checking.checker.model.OperationWithContext
import com.element.dpg.libs.chassis.openapi.checking.checker.model.allOperations
import com.element.dpg.libs.chassis.openapi.checking.checker.rule.OpenApiRule

class MandatoryRequestBodyContentMediaTypesRule(private val methodsToCheck: Set<HttpMethod>) : OpenApiRule {

    init {
        require(methodsToCheck.isNotEmpty()) { "Must specify at least one method to check" }
    }

    override fun invoke(api: OpenAPI): OpenApiRule.Result {

        val violations = api.allOperations().asSequence().filter { it.operation.method in methodsToCheck }.mapNotNull { operation -> check(operation) }.toSet()
        return OpenApiRule.Result.withViolations(violations)
    }

    private fun check(operation: OperationWithContext): Violation? {

        if (operation.isNotCompliant()) return operation.violation()
        return null
    }

    private fun OperationWithContext.violation() = Violation(this, methodsToCheck)

    private fun OperationWithContext.isNotCompliant(): Boolean = requestBody != null && requestBody.content.entries.isEmpty()

    data class Violation(val operation: OperationWithContext, val methodsToCheck: Set<HttpMethod>) : OpenApiRule.Result.Violation {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} should specify at least a media type for the content of the request body, but doesn't"
    }
}