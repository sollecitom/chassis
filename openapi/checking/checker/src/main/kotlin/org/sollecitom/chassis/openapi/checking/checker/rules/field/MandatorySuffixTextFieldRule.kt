package org.sollecitom.chassis.openapi.checking.checker.rules.field

import io.swagger.v3.oas.models.OpenAPI
import org.sollecitom.chassis.openapi.checking.checker.rule.OpenApiRule
import org.sollecitom.chassis.openapi.checking.checker.rule.field.FieldRule

data class MandatorySuffixTextFieldRule(val suffix: String, val ignoreCase: Boolean) : FieldRule<String, MandatorySuffixTextFieldRule.Violation> {

    override fun check(value: String, api: OpenAPI): Violation? {

        if (!value.endsWith(suffix = suffix, ignoreCase = ignoreCase)) return Violation(value, suffix, ignoreCase)
        return null
    }

    data class Violation(val value: String, val suffix: String, val ignoreCase: Boolean) : OpenApiRule.Result.Violation {

        override val message = "Value \"${value}\" should end with \"${suffix}\" ${if (ignoreCase) "case-insensitive" else "case-sensitive"}, but doesn't"
    }
}