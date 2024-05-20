package com.element.dpg.libs.chassis.openapi.validation.request.validator.tests

import assertk.assertThat
import com.atlassian.oai.validator.OpenApiInteractionValidator
import com.atlassian.oai.validator.model.SimpleRequest
import com.element.dpg.libs.chassis.openapi.builder.*
import com.element.dpg.libs.chassis.openapi.builder.OpenApiBuilder.OpenApiVersion.V3_1_0
import com.element.dpg.libs.chassis.openapi.validation.request.validator.test.utils.hasNoErrors
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class OpenApiRequestValidationTests {

    @Test
    fun `validating a request against the declared OpenAPI specification`() {

        val api = buildOpenApi {
            version(V3_1_0)
            path("/people") {
                get {
                    operationId("get_people")
                    description("Returns a list of people")
                    responses {
                        status(200) {
                            description("Successful response")
                            content {
                                mediaTypes {
                                    add("application/json")
                                }
                            }
                        }
                    }
                }
            }
        }
        val validator = OpenApiInteractionValidator.createFor(api).build()
        val request = SimpleRequest.Builder.get("/people").withAccept("application/json").build()

        val report = validator.validateRequest(request)

        assertThat(report).hasNoErrors()
    }
}