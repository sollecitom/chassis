package org.sollecitom.chassis.openapi.checking.checker.rules

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem.HttpMethod
import io.swagger.v3.oas.models.parameters.RequestBody
import org.sollecitom.chassis.openapi.checking.checker.model.OperationWithContext
import org.sollecitom.chassis.openapi.checking.checker.model.allOperations
import org.sollecitom.chassis.openapi.checking.checker.rule.OpenApiRule
import org.sollecitom.chassis.openapi.checking.checker.rule.RuleResult

class ForbiddenRequestBodyRule(private val methods: Set<HttpMethod>) : OpenApiRule {

    init {
        require(methods.isNotEmpty()) { "Must specify at least one method to check" }
    }

    override fun check(api: OpenAPI): RuleResult {

        val violations = api.allOperations().asSequence().filter { it.operation.method in methods }.mapNotNull { operation -> check(operation) }.toSet()
        return RuleResult.withViolations(violations)
    }

    private fun check(operation: OperationWithContext): Violation? {

        if (operation.isNotCompliant()) return operation.violation()
        return null
    }

    private fun OperationWithContext.violation() = Violation(this, requestBody!!)

    private fun OperationWithContext.isNotCompliant(): Boolean = requestBody != null

    data class Violation(val operation: OperationWithContext, val requestBody: RequestBody) : RuleResult.Violation {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} shouldn't specify a request body, but does"
    }
}