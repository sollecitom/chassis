package com.element.dpg.libs.chassis.openapi.checking.tests.sets.sets

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.element.dpg.libs.chassis.openapi.builder.get
import com.element.dpg.libs.chassis.openapi.builder.parameters
import com.element.dpg.libs.chassis.openapi.builder.post
import com.element.dpg.libs.chassis.openapi.builder.put
import com.element.dpg.libs.chassis.openapi.checking.checker.rules.DisallowReservedCharactersInParameterNameRule
import com.element.dpg.libs.chassis.openapi.checking.checker.rules.WhitelistedAlphabetParameterNameRule
import com.element.dpg.libs.chassis.openapi.checking.checker.sets.StandardOpenApiRules
import com.element.dpg.libs.chassis.openapi.checking.checker.sets.checkAgainstRules
import com.element.dpg.libs.chassis.openapi.checking.test.utils.isCompliant
import com.element.dpg.libs.chassis.openapi.checking.test.utils.isNotCompliantWithOnlyViolation
import io.swagger.v3.oas.models.PathItem
import org.junit.jupiter.api.Test

interface OperationParametersTestSpecification : OpenApiTestSpecification {

    val validParameterName: String
    val parameterLocation: String

    @Test
    fun `valid value works`() {

        val parameterName = validParameterName
        val api = openApi {
            path(validPath) {
                get {
                    operationId = validOperationId
                    summary = validSummary
                    parameters {
                        add {
                            name = parameterName
                            `in` = parameterLocation
                            required = false
                        }
                    }
                }
            }
        }

        val result = api.checkAgainstRules(StandardOpenApiRules)

        assertThat(result).isCompliant()
    }

    @Test
    fun `cannot contain numbers`() {

        val invalidParameterName = "param-123"
        val api = openApi {
            path(validPath) {
                get {
                    operationId = validOperationId
                    summary = validSummary
                    parameters {
                        add {
                            name = invalidParameterName
                            `in` = parameterLocation
                            required = false
                        }
                    }
                }
            }
        }

        val result = api.checkAgainstRules(StandardOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetParameterNameRule.Violation> { violation ->
            assertThat(violation.parameter.name).isEqualTo(invalidParameterName)
            assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
            assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
            assertThat(violation.parameter.location.operation.method).isEqualTo(PathItem.HttpMethod.GET)
        }
    }

    @Test
    fun `cannot contain symbols`() {

        val invalidParameterName = "param_id"
        val api = openApi {
            path(validPath) {
                get {
                    operationId = validOperationId
                    summary = validSummary
                    parameters {
                        add {
                            name = invalidParameterName
                            `in` = parameterLocation
                            required = false
                        }
                    }
                }
            }
        }

        val result = api.checkAgainstRules(StandardOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetParameterNameRule.Violation> { violation ->
            assertThat(violation.parameter.name).isEqualTo(invalidParameterName)
            assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
            assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
            assertThat(violation.parameter.location.operation.method).isEqualTo(PathItem.HttpMethod.GET)
        }
    }

    @Test
    fun `cannot allow reserved URL characters`() {

        val parameterName = validParameterName
        val api = openApi {
            path(validPath) {
                put {
                    operationId = validOperationId
                    summary = validSummary
                    setValidRequestBody()
                    parameters {
                        add {
                            name = parameterName
                            `in` = parameterLocation
                            required = false
                            allowReserved = true
                        }
                    }
                }
            }
        }

        val result = api.checkAgainstRules(StandardOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<DisallowReservedCharactersInParameterNameRule.Violation> { violation ->
            assertThat(violation.parameter.name).isEqualTo(parameterName)
            assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
            assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
            assertThat(violation.parameter.location.operation.method).isEqualTo(PathItem.HttpMethod.PUT)
        }
    }

    interface WithDisallowedUppercaseLetters : OperationParametersTestSpecification {

        @Test
        fun `cannot contain uppercase letters`() {

            val invalidParameterName = "PARAM-ID"
            val api = openApi {
                path(validPath) {
                    post {
                        operationId = validOperationId
                        summary = validSummary
                        setValidRequestBody()
                        parameters {
                            add {
                                name = invalidParameterName
                                `in` = parameterLocation
                                required = false
                            }
                        }
                    }
                }
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetParameterNameRule.Violation> { violation ->
                assertThat(violation.parameter.name).isEqualTo(invalidParameterName)
                assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
                assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
                assertThat(violation.parameter.location.operation.method).isEqualTo(PathItem.HttpMethod.POST)
            }
        }
    }
}