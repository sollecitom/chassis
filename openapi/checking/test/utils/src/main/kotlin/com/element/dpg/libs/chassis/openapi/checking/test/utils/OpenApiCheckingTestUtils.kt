package com.element.dpg.libs.chassis.openapi.checking.test.utils

import assertk.Assert
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import io.swagger.v3.oas.models.OpenAPI
import com.element.dpg.libs.chassis.openapi.checking.checker.ComplianceCheckResult
import com.element.dpg.libs.chassis.openapi.checking.checker.rule.OpenApiRule
import com.element.dpg.libs.chassis.openapi.checking.checker.rule.field.FieldRulesViolation
import com.element.dpg.libs.chassis.openapi.checking.checker.sets.OpenApiRuleSet
import com.element.dpg.libs.chassis.openapi.checking.checker.sets.checkAgainstRules
import com.element.dpg.libs.chassis.test.utils.assertions.containsSameElementsAs

fun Assert<OpenAPI>.isCompliantWith(firstRuleSet: OpenApiRuleSet, vararg otherRuleSets: OpenApiRuleSet) = given { api ->

    val result = api.checkAgainstRules(firstRuleSet, *otherRuleSets)
    assertThat(result).isCompliant()
}

fun Assert<ComplianceCheckResult>.isCompliant() = given { result ->

    if (result is ComplianceCheckResult.NonCompliant) {
        println(result.description())
    }
    assertThat(result).isEqualTo(ComplianceCheckResult.Compliant)
}

fun Assert<ComplianceCheckResult>.isNotCompliantWithOnlyViolation(expectedViolation: OpenApiRule.Result.Violation) = isNotCompliantWithViolations(expectedViolation)

@Suppress("UNCHECKED_CAST")
fun <VIOLATION : OpenApiRule.Result.Violation> Assert<ComplianceCheckResult>.isNotCompliantWithOnlyViolation(check: (VIOLATION) -> Unit) = given { result ->

    assertThat(result).isInstanceOf(ComplianceCheckResult.NonCompliant::class)
    with(result as ComplianceCheckResult.NonCompliant) {
        val violations = problems.flatMap { it.violations }
        assertThat(violations).hasSize(1)
        violations.single().let { it as VIOLATION }.apply(check)
    }
}

inline fun <reified VIOLATION : OpenApiRule.Result.Violation> Assert<ComplianceCheckResult>.isNotCompliantWithViolation(check: (VIOLATION) -> Unit) = given { result ->

    assertThat(result).isInstanceOf(ComplianceCheckResult.NonCompliant::class)
    with(result as ComplianceCheckResult.NonCompliant) {
        val violations = problems.flatMap { it.violations }
        violations.filterIsInstance<VIOLATION>().single().apply(check)
    }
}

@Suppress("UNCHECKED_CAST")
fun <VIOLATION : OpenApiRule.Result.Violation, VALUE : Any> Assert<FieldRulesViolation<VALUE>>.hasSingleFieldViolation(check: (VIOLATION) -> Unit) = given { violation ->

    assertThat(violation.fieldViolations).hasSize(1)
    val fieldViolation = violation.fieldViolations.single() as VIOLATION
    check(fieldViolation)
}

fun Assert<ComplianceCheckResult>.isNotCompliantWithViolations(vararg expectedViolations: OpenApiRule.Result.Violation) = isNotCompliantWithViolations(expectedViolations.toSet())

fun Assert<ComplianceCheckResult>.isNotCompliantWithViolations(expectedViolations: Set<OpenApiRule.Result.Violation>) = given { result ->

    assertThat(result).isInstanceOf(ComplianceCheckResult.NonCompliant::class)
    with(result as ComplianceCheckResult.NonCompliant) {
        val violations = problems.flatMap { it.violations }
        assertThat(violations.toSet()).containsSameElementsAs(expectedViolations)
    }
}