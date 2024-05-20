package com.element.dpg.libs.chassis.openapi.checking.checker.rule.field

import com.element.dpg.libs.chassis.openapi.checking.checker.model.OpenApiField
import com.element.dpg.libs.chassis.openapi.checking.checker.model.OperationWithContext
import com.element.dpg.libs.chassis.openapi.checking.checker.rule.OpenApiRule
import io.swagger.v3.oas.models.Operation

data class FieldRulesViolation<VALUE : Any>(val operation: OperationWithContext, val field: OpenApiField<Operation, VALUE?>, val rules: Set<FieldRule<*, *>>, val fieldViolations: Set<OpenApiRule.Result.Violation>) : OpenApiRule.Result.Violation {

    override val message = "Operation ${operation.operation.method} on path ${operation.pathName} should comply with rules $rules but doesn't. Violations where: ${fieldViolations.map(OpenApiRule.Result.Violation::message).joinToString("\n\t")}"
}