package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web

import assertk.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.example.service.endpoint.write.configuration.ApplicationProperties
import org.sollecitom.chassis.example.service.endpoint.write.configuration.configureLogging
import org.sollecitom.chassis.openapi.checking.checker.sets.StandardOpenApiRules
import org.sollecitom.chassis.openapi.checking.test.utils.isCompliantWith
import org.sollecitom.chassis.openapi.parser.OpenApiReader
import org.sollecitom.chassis.openapi.parser.parse
import org.sollecitom.chassis.resource.utils.ResourceLoader

@TestInstance(PER_CLASS)
private class OpenApiComplianceTests {

    init {
        configureLogging()
    }

    @Test
    fun `the declared OpenAPI specification complies with the standard OpenAPI guidelines`() {

        val apiFileLocation = ResourceLoader.resolve(ApplicationProperties.OPEN_API_FILE_LOCATION)

        val api = OpenApiReader.parse(apiFileLocation)

        assertThat(api).isCompliantWith(StandardOpenApiRules)
    }
}