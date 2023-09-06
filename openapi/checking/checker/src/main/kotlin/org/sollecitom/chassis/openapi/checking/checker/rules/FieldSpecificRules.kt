package org.sollecitom.chassis.openapi.checking.checker.rules

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import org.sollecitom.chassis.openapi.checking.checker.model.OpenApiField
import org.sollecitom.chassis.openapi.checking.checker.model.OperationWithContext
import org.sollecitom.chassis.openapi.checking.checker.model.allOperations
import org.sollecitom.chassis.openapi.checking.checker.rule.OpenApiRule
import org.sollecitom.chassis.openapi.checking.checker.rule.field.FieldRule
import org.sollecitom.chassis.openapi.checking.checker.rule.field.FieldRulesViolation

class FieldSpecificRules<VALUE : Any>(private val rulesByField: Map<OpenApiField<Operation, VALUE?>, Set<FieldRule<VALUE, *>>>) : OpenApiRule {

    override fun invoke(api: OpenAPI): OpenApiRule.Result {

        val violations = api.allOperations().asSequence().flatMap { operation -> operation.fields() }.mapNotNull { field -> check(field, api) }.toSet()
        return OpenApiRule.Result.withViolations(violations)
    }

    private fun check(field: FieldWithContext<VALUE?>, api: OpenAPI): FieldRulesViolation<VALUE>? {

        val fieldRules = rulesByField[field.field] ?: emptySet()
        val violations = fieldRules.mapNotNull { rule -> field.field.getter(field.operation.operation.operation)?.let { value -> rule.check(value, api) } }.toSet()
        return violations.takeUnless { it.isEmpty() }?.let { FieldRulesViolation(field.operation, field.field, fieldRules, violations) }
    }

    private fun OperationWithContext.fields(): Sequence<FieldWithContext<VALUE?>> = rulesByField.asSequence().map { FieldWithContext(it.key, this) }

    private data class FieldWithContext<VALUE>(val field: OpenApiField<Operation, VALUE>, val operation: OperationWithContext)

    companion object {

        operator fun <VALUE : Any> invoke(vararg fieldRules: Pair<OpenApiField<Operation, VALUE?>, Set<FieldRule<VALUE, *>>>) = FieldSpecificRules(fieldRules.toMap())
    }
}