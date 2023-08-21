package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web

import assertk.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.openapi.checking.checker.ComplianceCheckResult
import org.sollecitom.chassis.openapi.checking.checker.sets.StandardOpenApiRules
import org.sollecitom.chassis.openapi.checking.checker.sets.checkAgainstRules
import org.sollecitom.chassis.openapi.checking.test.utils.isCompliant
import org.sollecitom.chassis.openapi.parser.OpenApiReader
import org.sollecitom.chassis.openapi.parser.parse
import org.sollecitom.chassis.resource.utils.ResourceLoader

@TestInstance(PER_CLASS)
private class OpenApiComplianceTests {

    @Test
    fun `the declared OpenAPI specification complies with the standard OpenAPI guidelines`() {

        val apiFileLocation = ResourceLoader.resolve("api/api.yaml")
        val api = OpenApiReader.parse(apiFileLocation)

        val result = api.checkAgainstRules(StandardOpenApiRules)

        assertThat(result).isCompliant()
    }
}