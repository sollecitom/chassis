package com.element.dpg.libs.chassis.openapi.checking.checker.rules

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem.HttpMethod
import com.element.dpg.libs.chassis.openapi.checking.checker.model.OperationWithContext
import com.element.dpg.libs.chassis.openapi.checking.checker.model.allOperations
import com.element.dpg.libs.chassis.openapi.checking.checker.model.isRequired
import com.element.dpg.libs.chassis.openapi.checking.checker.rule.OpenApiRule


class MandatoryRequestBodyRule(methods: Set<Pair<HttpMethod, Boolean>>) : OpenApiRule {

    init {
        require(methods.isNotEmpty()) { "Must specify at least one method to check" }
    }

    private val methodsToCheck = methods.asSequence().map { it.first }.toSet()
    private val isBodyRequiredByMethod = methods.toMap()

    override fun invoke(api: OpenAPI): OpenApiRule.Result {

        val violations = api.allOperations().asSequence().filter { it.operation.method in methodsToCheck }.mapNotNull { operation -> check(operation) }.toSet()
        return OpenApiRule.Result.withViolations(violations)
    }

    private fun check(operation: OperationWithContext): Violation? {

        if (operation.isNotCompliant()) return operation.violation()
        return null
    }

    private fun OperationWithContext.violation() = Violation(this, isBodyRequiredByMethod[method]!!)

    private fun OperationWithContext.isNotCompliant(): Boolean = requestBody == null || (!requestBody.isRequired() && isBodyRequiredByMethod[operation.method]!!)

    data class Violation(val operation: OperationWithContext, val requiredBody: Boolean) : OpenApiRule.Result.Violation {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} should specify ${if (requiredBody) "a required" else "an optional"} request body, but doesn't"
    }
}