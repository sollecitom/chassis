package org.sollecitom.chassis.openapi.checking.checker.sets

import io.swagger.v3.oas.models.OpenAPI
import org.sollecitom.chassis.openapi.checking.checker.checkAgainstRules
import org.sollecitom.chassis.openapi.checking.checker.rule.OpenApiRule

interface OpenApiRuleSet {

    val rules: Set<OpenApiRule>
}

fun OpenAPI.checkAgainstRules(firstRuleSet: OpenApiRuleSet, vararg otherRuleSets: OpenApiRuleSet) = checkAgainstRules(rules = firstRuleSet.rules + otherRuleSets.flatMap(OpenApiRuleSet::rules).toSet())