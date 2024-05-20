package com.element.dpg.libs.chassis.openapi.checking.checker.rules

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import org.sollecitom.chassis.openapi.checking.checker.model.OpenApiField
import org.sollecitom.chassis.openapi.checking.checker.model.OperationWithContext
import org.sollecitom.chassis.openapi.checking.checker.model.allOperations
import org.sollecitom.chassis.openapi.checking.checker.rule.OpenApiRule
import org.sollecitom.chassis.openapi.checking.checker.rules.utils.trimmed

class MandatoryOperationFieldsRule(private val requiredFields: Set<OpenApiField<Operation, Any?>>) : OpenApiRule {

    override fun invoke(api: OpenAPI): OpenApiRule.Result {

        val violations = api.allOperations().asSequence().mapNotNull { operation -> check(operation) }.toSet()
        return OpenApiRule.Result.withViolations(violations)
    }

    private fun check(operation: OperationWithContext): Violation? {

        val missingRequiredFields = requiredFields.filter { field -> field.getter(operation.operation.operation)?.trimmed() == null }.toSet()
        if (missingRequiredFields.isNotEmpty()) return operation.violation(missingRequiredFields)
        return null
    }

    private fun OperationWithContext.violation(missingRequiredFields: Set<OpenApiField<Operation, Any?>>) = Violation(this, requiredFields, missingRequiredFields)

    private fun OperationWithContext.isNotCompliant(): Boolean = requiredFields.any { field -> field.getter(operation.operation)?.trimmed() == null }

    data class Violation(val operation: OperationWithContext, val requiredFields: Set<OpenApiField<Operation, Any?>>, val missingRequiredFields: Set<OpenApiField<Operation, Any?>>) : OpenApiRule.Result.Violation {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} should specify the following mandatory fields ${requiredFields.map(OpenApiField<Operation, *>::name)}, but fields ${missingRequiredFields.map(OpenApiField<Operation, *>::name)} were missing"
    }
}