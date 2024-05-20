package com.element.dpg.libs.chassis.openapi.checking.checker

import com.element.dpg.libs.chassis.openapi.checking.checker.rule.OpenApiRule
import io.swagger.v3.oas.models.OpenAPI

fun OpenAPI.checkAgainstRules(vararg rules: OpenApiRule): ComplianceCheckResult = checkAgainstRules(rules.toSet())

fun OpenAPI.checkAgainstRules(rules: Set<OpenApiRule>): ComplianceCheckResult {

    val checker = OpenApiComplianceChecker.withRules(rules)
    return checker.check(this)
}