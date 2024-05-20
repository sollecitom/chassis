package com.element.dpg.libs.chassis.openapi.checking.checker.rule

import io.swagger.v3.oas.models.OpenAPI

fun interface OpenApiRule {

    operator fun invoke(api: OpenAPI): Result

    sealed class Result {

        data object Compliant : Result()

        data class NonCompliant(val violations: Set<Violation>) : Result() {

            init {
                require(violations.isNotEmpty())
            }

            override fun toString() = "violations=$violations"
        }

        companion object {

            fun withViolations(violations: Set<Violation>): Result = if (violations.isEmpty()) Compliant else NonCompliant(violations)
            fun withViolationOrNull(violation: Violation?): Result = if (violation == null) Compliant else NonCompliant(setOf(violation))
        }

        interface Violation {
            val message: String
        }
    }
}