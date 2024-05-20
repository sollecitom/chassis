package com.element.dpg.libs.chassis.openapi.checking.checker

import com.element.dpg.libs.chassis.openapi.checking.checker.rule.OpenApiRule
import com.element.dpg.libs.chassis.openapi.parser.OpenApiReader
import com.element.dpg.libs.chassis.openapi.parser.parse
import io.swagger.v3.oas.models.OpenAPI
import java.net.URL

internal class ConfigurableOpenApiComplianceChecker(private val rules: Set<OpenApiRule>) : OpenApiComplianceChecker {

    override fun check(openApiRaw: String) = check(OpenApiReader.parseContent(openApiRaw))

    override fun check(openApiLocation: URL) = check(OpenApiReader.parse(openApiLocation))

    override fun check(openApi: OpenAPI): ComplianceCheckResult {

        val problems = mutableSetOf<OpenApiRule.Result.NonCompliant>()
        rules.forEach { rule ->
            val result = rule(openApi)
            if (result is OpenApiRule.Result.NonCompliant) {
                problems += result
            }
        }
        return if (problems.isEmpty()) ComplianceCheckResult.Compliant else ComplianceCheckResult.NonCompliant(problems)
    }
}

fun OpenApiComplianceChecker.Companion.withRules(rules: Set<OpenApiRule>): OpenApiComplianceChecker = ConfigurableOpenApiComplianceChecker(rules)