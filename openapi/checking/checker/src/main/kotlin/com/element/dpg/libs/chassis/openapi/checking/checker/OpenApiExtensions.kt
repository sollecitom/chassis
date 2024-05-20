package com.element.dpg.libs.chassis.openapi.checking.checker

import io.swagger.v3.oas.models.OpenAPI
import com.element.dpg.libs.chassis.openapi.checking.checker.rule.OpenApiRule

fun OpenAPI.checkAgainstRules(vararg rules: OpenApiRule): ComplianceCheckResult = checkAgainstRules(rules.toSet())

fun OpenAPI.checkAgainstRules(rules: Set<OpenApiRule>): ComplianceCheckResult {

    val checker = OpenApiComplianceChecker.withRules(rules)
    return checker.check(this)
}