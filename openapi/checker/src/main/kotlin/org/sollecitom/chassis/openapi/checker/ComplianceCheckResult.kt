package org.sollecitom.chassis.openapi.checker

import org.sollecitom.chassis.openapi.checker.rule.RuleResult

sealed class ComplianceCheckResult {

    object Compliant : ComplianceCheckResult()

    data class NonCompliant(val problems: Set<RuleResult.NonCompliant>) : ComplianceCheckResult()
}