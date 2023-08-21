package org.sollecitom.chassis.openapi.checker.rules

import io.swagger.v3.oas.models.OpenAPI
import org.sollecitom.chassis.openapi.checker.model.OperationWithContext
import org.sollecitom.chassis.openapi.checker.model.allOperations
import org.sollecitom.chassis.openapi.checker.rule.OpenApiRule
import org.sollecitom.chassis.openapi.checker.rule.RuleResult

object EnforceCamelCaseOperationIdRule : OpenApiRule {

    private const val camelCasePattern = "^[a-z][a-z0-9]*(([A-Z][a-z0-9]+)*[A-Z]?|([a-z0-9]+[A-Z])*|[A-Z])$"
    private val regex = camelCasePattern.toRegex()

    override fun check(api: OpenAPI): RuleResult {

        val violations = api.allOperations().asSequence().mapNotNull { operation -> check(operation) }.toSet()
        return RuleResult.withViolations(violations)
    }

    private fun check(operation: OperationWithContext): Violation? {

        if (operation.isNotCompliant()) return operation.violation()
        return null
    }

    private fun OperationWithContext.violation() = Violation(this)

    private fun OperationWithContext.isNotCompliant(): Boolean = !operationId.isNullOrBlank() && !operationId!!.isAssignableAsFunctionName()

    private fun String.isAssignableAsFunctionName(): Boolean = regex.matches(this)

    data class Violation(val operation: OperationWithContext) : RuleResult.Violation {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} should have an operationId in camelCase, but doesn't"
    }
}