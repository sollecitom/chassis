package org.sollecitom.chassis.openapi.checker.rules

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import org.sollecitom.chassis.openapi.checker.model.OpenApiField
import org.sollecitom.chassis.openapi.checker.model.OperationWithContext
import org.sollecitom.chassis.openapi.checker.model.allOperations
import org.sollecitom.chassis.openapi.checker.rule.OpenApiRule
import org.sollecitom.chassis.openapi.checker.rule.RuleResult

class MandatoryOperationFieldsRule(private val requiredFields: Set<OpenApiField<Operation, Any?>>) : OpenApiRule {

    override fun check(api: OpenAPI): RuleResult {

        val violations = api.allOperations().asSequence().mapNotNull { operation -> check(operation) }.toSet()
        return RuleResult.withViolations(violations)
    }

    private fun check(operation: OperationWithContext): Violation? {

        if (operation.isNotCompliant()) return operation.violation()
        return null
    }

    private fun OperationWithContext.violation() = Violation(this, requiredFields)

    private fun OperationWithContext.isNotCompliant(): Boolean = requiredFields.any { field -> field.getter(operation.operation)?.trimmed() == null }

    data class Violation(val operation: OperationWithContext, val requiredFields: Set<OpenApiField<Operation, Any?>>) : RuleResult.Violation {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} should specify the following mandatory fields ${requiredFields.map(OpenApiField<Operation, *>::name)}, but doesn't"
    }

    private fun Any.trimmed(): Any? = when (this) {
        is String -> takeIf { it.isNotBlank() }
        else -> this
    }
}