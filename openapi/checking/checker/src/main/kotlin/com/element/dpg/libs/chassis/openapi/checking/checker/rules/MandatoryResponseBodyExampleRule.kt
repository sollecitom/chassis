package com.element.dpg.libs.chassis.openapi.checking.checker.rules

import com.element.dpg.libs.chassis.openapi.checking.checker.model.OperationWithContext
import com.element.dpg.libs.chassis.openapi.checking.checker.model.allOperations
import com.element.dpg.libs.chassis.openapi.checking.checker.rule.OpenApiRule
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem.HttpMethod
import io.swagger.v3.oas.models.media.MediaType


class MandatoryResponseBodyExampleRule(private val methods: Set<HttpMethod>, private val mediaTypesThatShouldHaveAnExample: Set<String>) : OpenApiRule {

    init {
        require(methods.isNotEmpty()) { "Must specify at least one method to check" }
        require(mediaTypesThatShouldHaveAnExample.isNotEmpty()) { "Must specify at least one media type name to check" }
    }

    override fun invoke(api: OpenAPI): OpenApiRule.Result {

        val violations = api.allOperations().asSequence().filter { it.operation.method in methods }.mapNotNull { operation -> check(operation) }.toSet()
        return OpenApiRule.Result.withViolations(violations)
    }

    private fun check(operation: OperationWithContext): com.element.dpg.libs.chassis.openapi.checking.checker.rules.MandatoryResponseBodyExampleRule.Violation? {

        val responses = operation.responses?.takeUnless { it.isEmpty() } ?: return null
        val responsesWithoutAMandatoryExample = responses.mapNotNull { (status, response) ->
            val content = response.content ?: return@mapNotNull null
            val withoutMandatoryExample = mediaTypesThatShouldHaveAnExample.filter { mediaTypeName -> !content[mediaTypeName].isCompliant() }.toSet().takeUnless { it.isEmpty() } ?: return@mapNotNull null
            status to withoutMandatoryExample
        }.toMap()
        if (responsesWithoutAMandatoryExample.isNotEmpty()) return operation.violation(responsesWithoutAMandatoryExample)
        return null
    }

    private fun MediaType?.isCompliant(): Boolean = this == null || hasAnExample()
    private fun MediaType.hasAnExample(): Boolean = (examples?.isNotEmpty() ?: false) || example != null

    private fun OperationWithContext.violation(responsesWithoutAMandatoryExample: Map<String, Set<String>>) = com.element.dpg.libs.chassis.openapi.checking.checker.rules.MandatoryResponseBodyExampleRule.Violation(this, mediaTypesThatShouldHaveAnExample, responsesWithoutAMandatoryExample)

    class Violation(val operation: OperationWithContext, val mediaTypesThatShouldHaveAnExample: Set<String>, val responsesWithoutAMandatoryExample: Map<String, Set<String>>) : OpenApiRule.Result.Violation {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} should have a response body example for media types ${mediaTypesThatShouldHaveAnExample.joinToString(prefix = "[", postfix = "]")}, but some responses had no example for some media types. The offending responses are $responsesWithoutAMandatoryExample"
    }
}