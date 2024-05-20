package com.element.dpg.libs.chassis.openapi.checking.checker.rules

import io.swagger.v3.oas.models.OpenAPI
import com.element.dpg.libs.chassis.openapi.checking.checker.rule.OpenApiRule


class WhitelistedOpenApiVersionFieldRule(val whitelistedOpenApiVersions: Set<String>) : OpenApiRule {

    override fun invoke(api: OpenAPI): OpenApiRule.Result {

        val violation = check(api.openapi)
        return OpenApiRule.Result.withViolationOrNull(violation)
    }

    private fun check(version: String): Violation? {

        if (version !in whitelistedOpenApiVersions) {
            return Violation(version, whitelistedOpenApiVersions)
        }
        return null
    }

    data class Violation(val declaredVersion: String?, val whitelistedVersions: Set<String>) : OpenApiRule.Result.Violation {

        override val message = "OpenAPI version must be one of ${whitelistedVersions.joinToString(prefix = "[", postfix = "]")}, but was $declaredVersion"
    }
}