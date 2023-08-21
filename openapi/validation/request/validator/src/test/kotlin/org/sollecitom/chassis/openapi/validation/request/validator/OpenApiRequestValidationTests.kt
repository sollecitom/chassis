package org.sollecitom.chassis.openapi.validation.request.validator

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isFalse
import com.atlassian.oai.validator.OpenApiInteractionValidator
import com.atlassian.oai.validator.model.SimpleRequest
import com.atlassian.oai.validator.report.SimpleValidationReportFormat
import com.atlassian.oai.validator.report.ValidationReport
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.openapi.builder.*
import org.sollecitom.chassis.openapi.builder.OpenApiBuilder.OpenApiVersion.V3_1_0

@TestInstance(PER_CLASS)
private class OpenApiRequestValidationTests {

    // TODO test a file-based API
    // TODO add a test with a templated path parameter
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

// TODO move
fun Assert<ValidationReport>.hasNoErrors() = given { report ->

    if (report.hasErrors()) {
        println(SimpleValidationReportFormat.getInstance().apply(report))
    }
    assertk.assertThat(report.hasErrors()).isFalse()
}