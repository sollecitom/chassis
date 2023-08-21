package org.sollecitom.chassis.openapi.checker.sets

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.swagger.v3.oas.models.Operation
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.openapi.checker.*
import org.sollecitom.chassis.openapi.checker.model.OpenApiFields
import org.sollecitom.chassis.openapi.checker.rule.field.FieldRulesViolation
import org.sollecitom.chassis.openapi.checker.rules.*
import org.sollecitom.chassis.openapi.checker.rules.field.MandatorySuffixTextFieldRule

interface MethodTestSpecification : TracingHeadersTestSpecification {

    @Test
    fun `valid value works`() {

        val path = validPath
        val api = buildOpenApi {
            path(path) {
                operation(method) {
                    withValidFields()
                    withValidRequestBody()
                }
            }
        }

        val result = api.checkAgainstRules(StandardOpenApiRules)

        assertThat(result).isCompliant()
    }

    @Test
    fun `cannot omit operationId`() {

        val path = validPath
        val api = buildOpenApi {
            path(path) {
                operation(method) {
                    withValidFields()
                    withValidRequestBody()
                    operationId = null
                }
            }
        }

        val result = api.checkAgainstRules(StandardOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<MandatoryOperationFieldsRule.Violation> { violation ->
            assertThat(violation.operation.pathName).isEqualTo(path)
            assertThat(violation.operation.method).isEqualTo(method)
        }
    }

    @Test
    fun `cannot specify blank operationId`() {

        val path = validPath
        val api = buildOpenApi {
            path(path) {
                operation(method) {
                    withValidFields()
                    withValidRequestBody()
                    operationId = "   "
                }
            }
        }

        val result = api.checkAgainstRules(StandardOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<MandatoryOperationFieldsRule.Violation> { violation ->
            assertThat(violation.operation.pathName).isEqualTo(path)
            assertThat(violation.operation.method).isEqualTo(method)
        }
    }

    @Test
    fun `cannot omit summary`() {

        val path = validPath
        val api = buildOpenApi {
            path(path) {
                operation(method) {
                    withValidFields()
                    withValidRequestBody()
                    summary = null
                }
            }
        }

        val result = api.checkAgainstRules(StandardOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<MandatoryOperationFieldsRule.Violation> { violation ->
            assertThat(violation.operation.pathName).isEqualTo(path)
            assertThat(violation.operation.method).isEqualTo(method)
        }
    }

    @Test
    fun `cannot specify blank summary`() {

        val path = validPath
        val api = buildOpenApi {
            path(path) {
                operation(method) {
                    withValidFields()
                    withValidRequestBody()
                    summary = "   "
                }
            }
        }

        val result = api.checkAgainstRules(StandardOpenApiRules)

        assertThat(result).isNotCompliantWithViolation<MandatoryOperationFieldsRule.Violation> { violation ->
            assertThat(violation.operation.pathName).isEqualTo(path)
            assertThat(violation.operation.method).isEqualTo(method)
        }
    }

    @Test
    fun `cannot specify a description equal to the summary`() {

        val path = validPath
        val api = buildOpenApi {
            path(path) {
                operation(method) {
                    withValidFields()
                    withValidRequestBody()
                    description = summary
                }
            }
        }

        val result = api.checkAgainstRules(StandardOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<EnforceOperationDescriptionDifferentFromSummaryRule.Violation> { violation ->
            assertThat(violation.operation.pathName).isEqualTo(path)
            assertThat(violation.operation.method).isEqualTo(method)
        }
    }

    @Test
    fun `cannot specify an operationId which is not in camelCase`() {

        val path = validPath
        val api = buildOpenApi {
            path(path) {
                operation(method) {
                    withValidFields()
                    withValidRequestBody()
                    operationId = "NotCamelCase"
                }
            }
        }

        val result = api.checkAgainstRules(StandardOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<EnforceCamelCaseOperationIdRule.Violation> { violation ->
            assertThat(violation.operation.pathName).isEqualTo(path)
            assertThat(violation.operation.method).isEqualTo(method)
        }
    }

    @Test
    fun `cannot specify a summary that doesn't end with a full stop`() {

        val path = validPath
        val api = buildOpenApi {
            path(path) {
                operation(method) {
                    withValidFields()
                    withValidRequestBody()
                    summary = "A summary. Not ending with a full stop"
                }
            }
        }

        val result = api.checkAgainstRules(StandardOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<FieldRulesViolation<String>> { violation ->
            assertThat(violation.operation.pathName).isEqualTo(path)
            assertThat(violation.operation.method).isEqualTo(method)
            assertThat(violation.field).isEqualTo(OpenApiFields.Operation.summary)
            assertThat(violation).hasSingleFieldViolation<MandatorySuffixTextFieldRule.Violation, String> { fieldViolation ->
                assertThat(fieldViolation.suffix).isEqualTo(".")
            }
        }
    }

    @Test
    fun `cannot specify a description that doesn't end with a full stop`() {

        val path = validPath
        val api = buildOpenApi {
            path(path) {
                operation(method) {
                    withValidFields()
                    withValidRequestBody()
                    description = "A description. Not ending with a full stop"
                }
            }
        }

        val result = api.checkAgainstRules(StandardOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<FieldRulesViolation<String>> { violation ->
            assertThat(violation.operation.pathName).isEqualTo(path)
            assertThat(violation.operation.method).isEqualTo(method)
            assertThat(violation.field).isEqualTo(OpenApiFields.Operation.description)
            assertThat(violation).hasSingleFieldViolation<MandatorySuffixTextFieldRule.Violation, String> { fieldViolation ->
                assertThat(fieldViolation.suffix).isEqualTo(".")
            }
        }
    }

    interface WithoutRequestBody : MethodTestSpecification {

        override fun Operation.withValidRequestBody() {
            // no request body here
        }

        @Test
        fun `cannot specify request body`() {

            val path = validPath
            val api = buildOpenApi {
                path(path) {
                    operation(method) {
                        withValidFields()
                        requestBody {
                            withValidFields()
                        }
                    }
                }
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<ForbiddenRequestBodyRule.Violation> { violation ->
                assertThat(violation.operation.pathName).isEqualTo(path)
                assertThat(violation.operation.method).isEqualTo(method)
            }
        }
    }

    interface WithMandatoryRequestBody : MethodTestSpecification {

        override fun Operation.withValidRequestBody() {
            setValidRequestBody()
        }

        @Test
        fun `cannot omit request body`() {

            val path = validPath
            val api = buildOpenApi {
                path(path) {
                    operation(method) {
                        withValidFields()
                    }
                }
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<MandatoryRequestBodyRule.Violation> { violation ->
                assertThat(violation.operation.pathName).isEqualTo(path)
                assertThat(violation.operation.method).isEqualTo(method)
            }
        }

        @Test
        fun `cannot omit request body content media types`() {

            val path = validPath
            val api = buildOpenApi {
                path(path) {
                    operation(method) {
                        withValidFields()
                        requestBody {
                            withValidFields()
                            content {
                                // no media types declared here
                            }
                        }
                    }
                }
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<MandatoryRequestBodyContentMediaTypesRule.Violation> { violation ->
                assertThat(violation.operation.pathName).isEqualTo(path)
                assertThat(violation.operation.method).isEqualTo(method)
            }
        }

        @Test
        fun `cannot specify optional request body`() {

            val path = validPath
            val api = buildOpenApi {
                path(path) {
                    operation(method) {
                        withValidFields()
                        requestBody {
                            withValidFields()
                            required = false
                        }
                    }
                }
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<MandatoryRequestBodyRule.Violation> { violation ->
                assertThat(violation.operation.pathName).isEqualTo(path)
                assertThat(violation.operation.method).isEqualTo(method)
            }
        }

        @Test
        fun `cannot omit request body description`() {

            val path = validPath
            val api = buildOpenApi {
                path(path) {
                    operation(method) {
                        withValidFields()
                        requestBody {
                            withValidFields()
                            description = null
                        }
                    }
                }
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<MandatoryRequestBodyDescriptionRule.Violation> { violation ->
                assertThat(violation.operation.pathName).isEqualTo(path)
                assertThat(violation.operation.method).isEqualTo(method)
            }
        }

        @Test
        fun `cannot specify a blank request body description`() { // TODO add a test to enforce that the description for the request body ends with a full stop

            val path = validPath
            val api = buildOpenApi {
                path(path) {
                    operation(method) {
                        withValidFields()
                        requestBody {
                            withValidFields()
                            description = "   "
                        }
                    }
                }
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<MandatoryRequestBodyDescriptionRule.Violation> { violation ->
                assertThat(violation.operation.pathName).isEqualTo(path)
                assertThat(violation.operation.method).isEqualTo(method)
            }
        }
    }
}