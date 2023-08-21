package org.sollecitom.chassis.openapi.checker

import io.swagger.v3.oas.models.OpenAPI
import org.sollecitom.chassis.openapi.checker.rule.OpenApiRule
import org.sollecitom.chassis.openapi.checker.rule.RuleResult
import org.sollecitom.chassis.openapi.parser.OpenApiReader
import org.sollecitom.chassis.openapi.parser.parse
import java.net.URL

internal class ConfigurableOpenApiComplianceChecker(private val rules: Set<OpenApiRule>) : OpenApiComplianceChecker {

    override fun check(openApiRaw: String) = check(OpenApiReader.parseContent(openApiRaw))

    override fun check(openApiLocation: URL) = check(OpenApiReader.parse(openApiLocation))

    override fun check(openApi: OpenAPI): ComplianceCheckResult {

        val problems = mutableSetOf<RuleResult.NonCompliant>()
        rules.forEach { rule ->
            val result = rule.check(openApi)
            if (result is RuleResult.NonCompliant) {
                problems += result
            }
        }
        return if (problems.isEmpty()) ComplianceCheckResult.Compliant else ComplianceCheckResult.NonCompliant(problems)
    }
}

fun OpenApiComplianceChecker.Companion.withRules(rules: Set<OpenApiRule>): OpenApiComplianceChecker = ConfigurableOpenApiComplianceChecker(rules)