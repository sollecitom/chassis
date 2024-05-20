package com.element.dpg.libs.chassis.openapi.checking.checker.sets

import io.swagger.v3.oas.models.OpenAPI
import com.element.dpg.libs.chassis.openapi.checking.checker.checkAgainstRules
import com.element.dpg.libs.chassis.openapi.checking.checker.rule.OpenApiRule

interface OpenApiRuleSet {

    val rules: Set<OpenApiRule>
}

fun OpenAPI.checkAgainstRules(firstRuleSet: OpenApiRuleSet, vararg otherRuleSets: OpenApiRuleSet) = checkAgainstRules(rules = firstRuleSet.rules + otherRuleSets.flatMap(OpenApiRuleSet::rules).toSet())