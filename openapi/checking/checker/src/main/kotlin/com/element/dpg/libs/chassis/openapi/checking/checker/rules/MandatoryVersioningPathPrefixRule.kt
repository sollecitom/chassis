package com.element.dpg.libs.chassis.openapi.checking.checker.rules

import io.swagger.v3.oas.models.OpenAPI
import com.element.dpg.libs.chassis.openapi.checking.checker.rule.OpenApiRule


class MandatoryVersioningPathPrefixRule(private val minimumAllowedVersion: Int = 1) : OpenApiRule {

    val prefixRegex: Regex get() = Companion.prefixRegex

    override fun invoke(api: OpenAPI): OpenApiRule.Result {

        val violations = api.paths.asSequence().map { it.key }.mapNotNull { path -> check(path) }.toSet()
        return OpenApiRule.Result.withViolations(violations)
    }

    private fun check(path: String): Violation? {

        if (path.isNotCompliant()) return path.violation()
        return null
    }

    private fun String.violation() = Violation(this, minimumAllowedVersion)

    private fun String.isNotCompliant(): Boolean {

        regex.find(this) ?: return true
        val prefixMatch = prefixRegex.find(this)!!
        val version = prefixMatch.groupValues.drop(1).single().toInt()
        if (version < minimumAllowedVersion) return true
        return false
    }

    data class Violation(val pathName: String, val minimumAllowedVersion: Int) : OpenApiRule.Result.Violation {

        override val message = "Path $pathName should start with versioning prefix e.g. \"v${minimumAllowedVersion}\" and specify a version greater than or equal to $minimumAllowedVersion, but doesn't"
    }

    companion object {
        private val prefixRegex = "/v([0-9])".toRegex()
        private val regex = "/v([0-9]/.+)".toRegex()
    }
}