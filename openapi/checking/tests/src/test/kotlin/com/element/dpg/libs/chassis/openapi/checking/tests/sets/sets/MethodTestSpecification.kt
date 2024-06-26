package com.element.dpg.libs.chassis.openapi.checking.tests.sets.sets

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.element.dpg.libs.chassis.openapi.builder.content
import com.element.dpg.libs.chassis.openapi.builder.operation
import com.element.dpg.libs.chassis.openapi.builder.requestBody
import com.element.dpg.libs.chassis.openapi.checking.checker.model.OpenApiFields
import com.element.dpg.libs.chassis.openapi.checking.checker.rule.field.FieldRulesViolation
import com.element.dpg.libs.chassis.openapi.checking.checker.rules.*
import com.element.dpg.libs.chassis.openapi.checking.checker.rules.field.MandatorySuffixTextFieldRule
import com.element.dpg.libs.chassis.openapi.checking.checker.sets.StandardOpenApiRules
import com.element.dpg.libs.chassis.openapi.checking.checker.sets.checkAgainstRules
import com.element.dpg.libs.chassis.openapi.checking.test.utils.hasSingleFieldViolation
import com.element.dpg.libs.chassis.openapi.checking.test.utils.isCompliant
import com.element.dpg.libs.chassis.openapi.checking.test.utils.isNotCompliantWithOnlyViolation
import com.element.dpg.libs.chassis.openapi.checking.test.utils.isNotCompliantWithViolation
import io.swagger.v3.oas.models.Operation
import org.junit.jupiter.api.Test

interface MethodTestSpecification : TracingHeadersTestSpecification {

    @Test
    fun `valid value works`() {

        val path = validPath
        val api = openApi {
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
        val api = openApi {
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
        val api = openApi {
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
        val api = openApi {
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
        val api = openApi {
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
        val api = openApi {
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
        val api = openApi {
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
        val api = openApi {
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
        val api = openApi {
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
            val api = openApi {
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
            val api = openApi {
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
            val api = openApi {
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
            val api = openApi {
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
            val api = openApi {
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
        fun `cannot specify a request body description that doesn't end with a full stop`() {

            val path = validPath
            val api = openApi {
                path(path) {
                    operation(method) {
                        withValidFields()
                        requestBody {
                            withValidFields()
                            description = "A request body description not ending with a full stop"
                        }
                    }
                }
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<FieldRulesViolation<String>> { violation ->
                assertThat(violation.operation.pathName).isEqualTo(path)
                assertThat(violation.operation.method).isEqualTo(method)
                assertThat(violation.field).isEqualTo(OpenApiFields.Operation.RequestBody.description)
                assertThat(violation).hasSingleFieldViolation<MandatorySuffixTextFieldRule.Violation, String> { fieldViolation ->
                    assertThat(fieldViolation.suffix).isEqualTo(".")
                }
            }
        }
    }
}