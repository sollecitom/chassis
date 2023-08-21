package org.sollecitom.chassis.openapi.checking.checker.rule

sealed class RuleResult { // TODO put inside Rule?

    object Compliant : RuleResult()

    data class NonCompliant(val violations: Set<Violation>) : RuleResult() {

        init {
            require(violations.isNotEmpty())
        }
    }

    companion object {

        fun withViolations(violations: Set<Violation>): RuleResult = if (violations.isEmpty()) Compliant else NonCompliant(violations)
        fun withViolationOrNull(violation: Violation?): RuleResult = if (violation == null) Compliant else NonCompliant(setOf(violation))
    }

    interface Violation {
        val message: String
    }
}