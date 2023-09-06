package org.sollecitom.chassis.openapi.checking.checker

import org.sollecitom.chassis.openapi.checking.checker.rule.OpenApiRule

sealed class ComplianceCheckResult {

    data object Compliant : ComplianceCheckResult()

    data class NonCompliant(val problems: Set<OpenApiRule.Result.NonCompliant>) : ComplianceCheckResult() {

        fun description(): String = problems.joinToString(separator = "\n", prefix = "Compliance problems:\n") { problem ->
            problem.violations.joinToString(separator = "\n\t- ", prefix = "\t- ", transform = OpenApiRule.Result.Violation::message)
        }
    }
}