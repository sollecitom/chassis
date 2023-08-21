package org.sollecitom.chassis.openapi.checker.rules

import io.swagger.v3.oas.models.OpenAPI
import org.sollecitom.chassis.openapi.checker.rule.OpenApiRule
import org.sollecitom.chassis.openapi.checker.rule.RuleResult

object LowercasePathNameRule : OpenApiRule {

    override fun check(api: OpenAPI): RuleResult {

        val violations = api.paths.asSequence().mapNotNull { (pathName, _) -> check(pathName) }.toSet()
        return RuleResult.withViolations(violations)
    }

    private fun check(pathName: String): Violation? = pathName.takeIf { it.lowercase() != it }?.let { Violation(it) }

    data class Violation(val path: String) : RuleResult.Violation {

        override val message = "Path $path should be lowercase but isn't"
    }
}