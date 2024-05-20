package com.element.dpg.libs.chassis.openapi.checking.checker.rule.field

import com.element.dpg.libs.chassis.openapi.checking.checker.rule.OpenApiRule
import io.swagger.v3.oas.models.OpenAPI

fun interface FieldRule<in VALUE, out VIOLATION : OpenApiRule.Result.Violation> {

    fun check(value: VALUE, api: OpenAPI): VIOLATION?
}