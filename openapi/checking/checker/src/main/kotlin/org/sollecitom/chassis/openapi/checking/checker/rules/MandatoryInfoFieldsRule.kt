package org.sollecitom.chassis.openapi.checking.checker.rules

import io.swagger.v3.oas.models.OpenAPI
import org.sollecitom.chassis.openapi.checking.checker.model.OpenApiField
import org.sollecitom.chassis.openapi.checking.checker.rule.OpenApiRule
import org.sollecitom.chassis.openapi.checking.checker.rule.RuleResult
import org.sollecitom.chassis.openapi.checking.checker.rules.utils.trimmed
import io.swagger.v3.oas.models.info.Info as OpenApiInfo

class MandatoryInfoFieldsRule(private val requiredFields: Set<OpenApiField<OpenApiInfo, Any?>>) : OpenApiRule {

    override fun check(api: OpenAPI): RuleResult {

        val violation = check(api.info)
        return RuleResult.withViolationOrNull(violation)
    }

    private fun check(info: OpenApiInfo?): Violation? {

        val missingRequiredFields = requiredFields.filter { field -> info?.let { field.getter(it)?.trimmed() == null } ?: true }.toSet()
        if (missingRequiredFields.isNotEmpty()) return Violation(requiredFields, missingRequiredFields)
        return null
    }

    data class Violation(val requiredFields: Set<OpenApiField<OpenApiInfo, Any?>>, val missingRequiredFields: Set<OpenApiField<OpenApiInfo, Any?>>) : RuleResult.Violation {

        override val message = "Info section should specify the following mandatory fields ${requiredFields.map(OpenApiField<OpenApiInfo, *>::name)}, but fields ${missingRequiredFields.map(OpenApiField<OpenApiInfo, *>::name)} were missing"
    }
}