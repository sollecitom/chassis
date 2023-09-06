package org.sollecitom.chassis.openapi.checking.checker.rule.field

import org.sollecitom.chassis.openapi.checking.checker.rule.OpenApiRule

fun interface FieldRule<in VALUE, out VIOLATION : OpenApiRule.Result.Violation> {

    // TODO pass OpenApi here as well? otherwise just make Rule generic and remove the 2 different types
    fun check(value: VALUE): VIOLATION?
}