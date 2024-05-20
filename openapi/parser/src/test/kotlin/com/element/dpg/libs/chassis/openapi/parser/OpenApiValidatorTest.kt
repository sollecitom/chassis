package com.element.dpg.libs.chassis.openapi.parser

import assertk.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class OpenApiValidatorTest {

    @Test
    fun `validator confirms that valid OpenAPI specification is valid`() {

        val validator = newValidator()

        val validOpenApiLocation = "api/ValidSwagger.yml"
        val result = validator.validate(validOpenApiLocation)

        assertThat(result).isValid()
    }

    @Test
    fun `validator confirms that invalid OpenAPI specification is invalid`() {

        val validator = newValidator()

        val invalidOpenApiLocation = "api/InvalidSwagger.yml"
        val result = validator.validate(invalidOpenApiLocation)

        assertThat(result).isInvalid()
    }

    private fun newValidator(): OpenApiValidator = OpenApiReader
}