package org.sollecitom.chassis.openapi.checker.rules.field

import org.sollecitom.chassis.openapi.checker.rule.RuleResult
import org.sollecitom.chassis.openapi.checker.rule.field.FieldRule

data class MandatorySuffixTextFieldRule(val suffix: String, val ignoreCase: Boolean) : FieldRule<String, MandatorySuffixTextFieldRule.Violation> {

    override fun check(value: String): Violation? {

        if (!value.endsWith(suffix = suffix, ignoreCase = ignoreCase)) return Violation(value, suffix, ignoreCase)
        return null
    }

    data class Violation(val value: String, val suffix: String, val ignoreCase: Boolean) : RuleResult.Violation {

        override val message = "Value \"${value}\" should end with \"${suffix}\" ${if (ignoreCase) "case-insensitive" else "case-sensitive"}, but doesn't"
    }
}