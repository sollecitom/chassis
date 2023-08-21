package org.sollecitom.chassis.openapi.checking.checker.rules

import io.swagger.v3.oas.models.OpenAPI
import org.sollecitom.chassis.openapi.checking.checker.rule.OpenApiRule
import org.sollecitom.chassis.openapi.checking.checker.rule.RuleResult

class WhitelistedOpenApiVersionFieldRule(val whitelistedOpenApiVersions: Set<String>) : OpenApiRule {

    override fun check(api: OpenAPI): RuleResult {

        val violation = check(api.openapi)
        return RuleResult.withViolationOrNull(violation)
    }

    private fun check(version: String): Violation? {

        if (version !in whitelistedOpenApiVersions) {
            return Violation(version, whitelistedOpenApiVersions)
        }
        return null
    }

    data class Violation(val declaredVersion: String?, val whitelistedVersions: Set<String>) : RuleResult.Violation {

        override val message = "OpenAPI version must be one of ${whitelistedVersions.joinToString(prefix = "[", postfix = "]")}, but was $declaredVersion"
    }
}