package org.sollecitom.chassis.openapi.checker.rule.field

import org.sollecitom.chassis.openapi.checker.rule.RuleResult

fun interface FieldRule<in VALUE, out VIOLATION : RuleResult.Violation> {

    // TODO pass OpenApi here as well? otherwise just make Rule generic and remove the 2 different types
    fun check(value: VALUE): VIOLATION?
}