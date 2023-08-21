package org.sollecitom.chassis.openapi.checker.rules

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import org.sollecitom.chassis.openapi.checker.model.OpenApiField
import org.sollecitom.chassis.openapi.checker.model.OperationWithContext
import org.sollecitom.chassis.openapi.checker.model.allOperations
import org.sollecitom.chassis.openapi.checker.rule.OpenApiRule
import org.sollecitom.chassis.openapi.checker.rule.RuleResult
import org.sollecitom.chassis.openapi.checker.rule.field.FieldRule
import org.sollecitom.chassis.openapi.checker.rule.field.FieldRulesViolation

class CompositeFieldRule<VALUE : Any>(private val rulesByField: Map<OpenApiField<Operation, VALUE?>, Set<FieldRule<VALUE, *>>>) : OpenApiRule {

    override fun check(api: OpenAPI): RuleResult {

        val violations = api.allOperations().asSequence().flatMap { operation -> operation.fields() }.mapNotNull { field -> check(field) }.toSet()
        return RuleResult.withViolations(violations)
    }

    private fun check(field: FieldWithContext<VALUE?>): FieldRulesViolation<VALUE>? {

        val fieldRules = rulesByField[field.field] ?: emptySet()
        val violations = fieldRules.mapNotNull { rule -> field.field.getter(field.operation.operation.operation)?.let { value -> rule.check(value) } }.toSet()
        return violations.takeUnless { it.isEmpty() }?.let { FieldRulesViolation(field.operation, field.field, fieldRules, violations) }
    }

    private fun OperationWithContext.fields(): Sequence<FieldWithContext<VALUE?>> = rulesByField.asSequence().map { FieldWithContext(it.key, this) }

    private data class FieldWithContext<VALUE>(val field: OpenApiField<Operation, VALUE>, val operation: OperationWithContext)
}