package com.element.dpg.libs.chassis.openapi.parser

import assertk.assertThat
import assertk.assertions.isSuccess
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import com.element.dpg.libs.chassis.test.utils.assertions.failedThrowing

@TestInstance(PER_CLASS)
private class OpenApiParserTest {

    @Test
    fun `parser is able to read and construct OpenAPI object`() {

        val parser = newOpenApiParser()
        val validOpenApiLocation = "api/ValidSwagger.yml"

        val result = runCatching { parser.parse(validOpenApiLocation) }

        assertThat(result).isSuccess()
    }

    @Test
    fun `parser is able to read and construct OpenAPI object via its URL`() {

        val parser = newOpenApiParser()
        val validOpenApiLocation = readResourceWithName("api/ValidSwagger.yml")

        val result = runCatching { parser.parse(validOpenApiLocation) }

        assertThat(result).isSuccess()
    }

    @Test
    fun `parser is unable to read and construct OpenAPI invalid object`() {

        val parser = newOpenApiParser()
        val invalidOpenApiLocation = "api/InvalidSwagger.yml"

        val result = runCatching { parser.parse(invalidOpenApiLocation) }

        assertThat(result).failedThrowing<OpenApiParser.ParseException>()
    }

    private fun newOpenApiParser(): OpenApiParser = OpenApiReader
}