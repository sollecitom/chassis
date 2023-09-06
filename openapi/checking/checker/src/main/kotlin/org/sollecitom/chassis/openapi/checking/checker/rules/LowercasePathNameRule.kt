package org.sollecitom.chassis.openapi.checking.checker.rules

import io.swagger.v3.oas.models.OpenAPI
import org.sollecitom.chassis.openapi.checking.checker.rule.OpenApiRule

object LowercasePathNameRule : OpenApiRule {

    override fun invoke(api: OpenAPI): OpenApiRule.Result {

        val violations = api.paths.asSequence().mapNotNull { (pathName, _) -> check(pathName) }.toSet()
        return OpenApiRule.Result.withViolations(violations)
    }

    private fun check(pathName: String): Violation? = pathName.takeIf { it.lowercase() != it }?.let { Violation(it) }

    data class Violation(val path: String) : OpenApiRule.Result.Violation {

        override val message = "Path $path should be lowercase but isn't"
    }
}