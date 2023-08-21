package org.sollecitom.chassis.openapi.checker.rule

import io.swagger.v3.oas.models.OpenAPI

fun interface OpenApiRule { // TODO rename to Check?

    // TODO make it invoke instead?
    fun check(api: OpenAPI): RuleResult
}