package org.sollecitom.chassis.openapi.checking.checker.rules

import io.swagger.v3.oas.models.OpenAPI
import org.sollecitom.chassis.openapi.checking.checker.model.OperationWithContext
import org.sollecitom.chassis.openapi.checking.checker.model.allOperations
import org.sollecitom.chassis.openapi.checking.checker.rule.OpenApiRule

object EnforceOperationDescriptionDifferentFromSummaryRule : OpenApiRule {

    override fun invoke(api: OpenAPI): OpenApiRule.Result {

        val violations = api.allOperations().asSequence().mapNotNull { operation -> check(operation) }.toSet()
        return OpenApiRule.Result.withViolations(violations)
    }

    private fun check(operation: OperationWithContext): Violation? {

        if (operation.isNotCompliant()) return operation.violation()
        return null
    }

    private fun OperationWithContext.violation() = Violation(this)

    private fun OperationWithContext.isNotCompliant(): Boolean = description != null && description == summary

    data class Violation(val operation: OperationWithContext) : OpenApiRule.Result.Violation {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} shouldn't have a description equal to its summary, but does"
    }
}