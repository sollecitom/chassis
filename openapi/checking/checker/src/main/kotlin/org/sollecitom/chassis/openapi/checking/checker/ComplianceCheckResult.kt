package org.sollecitom.chassis.openapi.checking.checker

import org.sollecitom.chassis.openapi.checking.checker.rule.RuleResult

sealed class ComplianceCheckResult {

    object Compliant : ComplianceCheckResult()

    data class NonCompliant(val problems: Set<RuleResult.NonCompliant>) : ComplianceCheckResult() {

        fun description(): String = problems.joinToString(separator = "\n", prefix = "Compliance problems:\n") { problem ->
            problem.violations.joinToString(separator = "\n\t- ", prefix = "\t- ", transform = RuleResult.Violation::message)
        }
    }
}