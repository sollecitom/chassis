package com.element.dpg.libs.chassis.openapi.checking.checker.rules

import com.element.dpg.libs.chassis.openapi.checking.checker.model.OperationWithContext
import com.element.dpg.libs.chassis.openapi.checking.checker.model.allOperations
import com.element.dpg.libs.chassis.openapi.checking.checker.rule.OpenApiRule
import io.swagger.v3.oas.models.OpenAPI

object EnforceCamelCaseOperationIdRule : OpenApiRule {

    private const val camelCasePattern = "^[a-z][a-z0-9]*(([A-Z][a-z0-9]+)*[A-Z]?|([a-z0-9]+[A-Z])*|[A-Z])$"
    private val regex = camelCasePattern.toRegex()

    override fun invoke(api: OpenAPI): OpenApiRule.Result {

        val violations = api.allOperations().asSequence().mapNotNull { operation -> check(operation) }.toSet()
        return OpenApiRule.Result.withViolations(violations)
    }

    private fun check(operation: OperationWithContext): Violation? {

        if (operation.isNotCompliant()) return operation.violation()
        return null
    }

    private fun OperationWithContext.violation() = Violation(this)

    private fun OperationWithContext.isNotCompliant(): Boolean = !operationId.isNullOrBlank() && !operationId!!.isAssignableAsFunctionName()

    private fun String.isAssignableAsFunctionName(): Boolean = regex.matches(this)

    data class Violation(val operation: OperationWithContext) : OpenApiRule.Result.Violation {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} should have an operationId in camelCase, but doesn't"
    }
}