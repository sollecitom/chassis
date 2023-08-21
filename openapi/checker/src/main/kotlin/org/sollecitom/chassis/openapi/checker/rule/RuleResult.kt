package org.sollecitom.chassis.openapi.checker.rule

sealed class RuleResult { // TODO put inside Rule?

    object Compliant : RuleResult()

    data class NonCompliant(val violations: Set<Violation>) : RuleResult() {

        init {
            require(violations.isNotEmpty())
        }
    }

    companion object {

        fun withViolations(violations: Set<Violation>): RuleResult = if (violations.isEmpty()) Compliant else NonCompliant(violations)
    }

    interface Violation {
        val message: String
    }
}