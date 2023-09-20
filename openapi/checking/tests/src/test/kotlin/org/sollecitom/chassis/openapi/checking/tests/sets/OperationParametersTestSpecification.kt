package org.sollecitom.chassis.openapi.checking.tests.sets

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.swagger.v3.oas.models.PathItem
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.openapi.builder.get
import org.sollecitom.chassis.openapi.builder.parameters
import org.sollecitom.chassis.openapi.builder.post
import org.sollecitom.chassis.openapi.builder.put
import org.sollecitom.chassis.openapi.checking.checker.rules.DisallowReservedCharactersInParameterNameRule
import org.sollecitom.chassis.openapi.checking.checker.rules.WhitelistedAlphabetParameterNameRule
import org.sollecitom.chassis.openapi.checking.checker.sets.StandardOpenApiRules
import org.sollecitom.chassis.openapi.checking.checker.sets.checkAgainstRules
import org.sollecitom.chassis.openapi.checking.test.utils.isCompliant
import org.sollecitom.chassis.openapi.checking.test.utils.isNotCompliantWithOnlyViolation

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