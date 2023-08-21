package org.sollecitom.chassis.openapi.checker.rule.field

import io.swagger.v3.oas.models.Operation
import org.sollecitom.chassis.openapi.checker.model.OpenApiField
import org.sollecitom.chassis.openapi.checker.model.OperationWithContext
import org.sollecitom.chassis.openapi.checker.rule.RuleResult

data class FieldRulesViolation<VALUE : Any>(val operation: OperationWithContext, val field: OpenApiField<Operation, VALUE?>, val rules: Set<FieldRule<*, *>>, val fieldViolations: Set<RuleResult.Violation>) : RuleResult.Violation {

    override val message = "Operation ${operation.operation.method} on path ${operation.pathName} should comply with rules $rules but doesn't. Violations where: ${fieldViolations.map(RuleResult.Violation::message).joinToString("\n\t")}"
}