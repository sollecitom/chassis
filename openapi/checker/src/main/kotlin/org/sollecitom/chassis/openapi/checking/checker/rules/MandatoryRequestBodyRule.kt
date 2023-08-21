package org.sollecitom.chassis.openapi.checking.checker.rules

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem.HttpMethod
import org.sollecitom.chassis.openapi.checking.checker.model.OperationWithContext
import org.sollecitom.chassis.openapi.checking.checker.model.allOperations
import org.sollecitom.chassis.openapi.checking.checker.model.isRequired
import org.sollecitom.chassis.openapi.checking.checker.rule.OpenApiRule
import org.sollecitom.chassis.openapi.checking.checker.rule.RuleResult

class MandatoryRequestBodyRule(methods: Set<Pair<HttpMethod, Boolean>>) : OpenApiRule {

    init {
        require(methods.isNotEmpty()) { "Must specify at least one method to check" }
    }

    private val methodsToCheck = methods.asSequence().map { it.first }.toSet()
    private val isBodyRequiredByMethod = methods.toMap()

    override fun check(api: OpenAPI): RuleResult {

        val violations = api.allOperations().asSequence().filter { it.operation.method in methodsToCheck }.mapNotNull { operation -> check(operation) }.toSet()
        return RuleResult.withViolations(violations)
    }

    private fun check(operation: OperationWithContext): Violation? {

        if (operation.isNotCompliant()) return operation.violation()
        return null
    }

    private fun OperationWithContext.violation() = Violation(this, isBodyRequiredByMethod[method]!!)

    private fun OperationWithContext.isNotCompliant(): Boolean = requestBody == null || (!requestBody.isRequired() && isBodyRequiredByMethod[operation.method]!!)

    data class Violation(val operation: OperationWithContext, val requiredBody: Boolean) : RuleResult.Violation {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} should specify ${if (requiredBody) "a required" else "an optional"} request body, but doesn't"
    }
}