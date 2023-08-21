package org.sollecitom.chassis.openapi.checking.checker

import assertk.Assert
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import org.sollecitom.chassis.openapi.checking.checker.rule.RuleResult
import org.sollecitom.chassis.openapi.checking.checker.rule.field.FieldRulesViolation
import org.sollecitom.chassis.test.utils.assertions.containsSameElementsAs

// TODO move somewhere, so we can use this in other modules
fun Assert<ComplianceCheckResult>.isCompliant() = given { result ->

    assertThat(result).isEqualTo(ComplianceCheckResult.Compliant)
}

fun Assert<ComplianceCheckResult>.isNotCompliantWithOnlyViolation(expectedViolation: RuleResult.Violation) = isNotCompliantWithViolations(expectedViolation)

@Suppress("UNCHECKED_CAST")
fun <VIOLATION : RuleResult.Violation> Assert<ComplianceCheckResult>.isNotCompliantWithOnlyViolation(check: (VIOLATION) -> Unit) = given { result ->

    assertThat(result).isInstanceOf(ComplianceCheckResult.NonCompliant::class)
    with(result as ComplianceCheckResult.NonCompliant) {
        val violations = problems.flatMap { it.violations }
        violations.single().let { it as VIOLATION }.apply(check)
    }
}

inline fun <reified VIOLATION : RuleResult.Violation> Assert<ComplianceCheckResult>.isNotCompliantWithViolation(check: (VIOLATION) -> Unit) = given { result ->

    assertThat(result).isInstanceOf(ComplianceCheckResult.NonCompliant::class)
    with(result as ComplianceCheckResult.NonCompliant) {
        val violations = problems.flatMap { it.violations }
        violations.filterIsInstance<VIOLATION>().single().apply(check)
    }
}

@Suppress("UNCHECKED_CAST")
fun <VIOLATION : RuleResult.Violation, VALUE : Any> Assert<FieldRulesViolation<VALUE>>.hasSingleFieldViolation(check: (VIOLATION) -> Unit) = given { violation ->

    assertThat(violation.fieldViolations).hasSize(1)
    val fieldViolation = violation.fieldViolations.single() as VIOLATION
    check(fieldViolation)
}

fun Assert<ComplianceCheckResult>.isNotCompliantWithViolations(vararg expectedViolations: RuleResult.Violation) = isNotCompliantWithViolations(expectedViolations.toSet())

fun Assert<ComplianceCheckResult>.isNotCompliantWithViolations(expectedViolations: Set<RuleResult.Violation>) = given { result ->

    assertThat(result).isInstanceOf(ComplianceCheckResult.NonCompliant::class)
    with(result as ComplianceCheckResult.NonCompliant) {
        val violations = problems.flatMap { it.violations }
        assertThat(violations).containsSameElementsAs(expectedViolations.toSet())
    }
}