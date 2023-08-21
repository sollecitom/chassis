package org.sollecitom.chassis.openapi.checking.checker

import org.sollecitom.chassis.openapi.checking.checker.rule.RuleResult

sealed class ComplianceCheckResult {

    object Compliant : ComplianceCheckResult()

    data class NonCompliant(val problems: Set<RuleResult.NonCompliant>) : ComplianceCheckResult()
}