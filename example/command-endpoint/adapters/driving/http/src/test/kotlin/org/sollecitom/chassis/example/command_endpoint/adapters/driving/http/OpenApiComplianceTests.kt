package org.sollecitom.chassis.example.command_endpoint.adapters.driving.http

import assertk.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.logging.standard.configuration.configureLogging
import org.sollecitom.chassis.openapi.checking.checker.sets.StandardOpenApiRules
import org.sollecitom.chassis.openapi.checking.test.utils.isCompliantWith

@TestInstance(PER_CLASS)
private class OpenApiComplianceTests {

    init {
        configureLogging()
    }

    @Test
    fun `the declared OpenAPI specification complies with the standard OpenAPI guidelines`() {

        assertThat(openApi).isCompliantWith(StandardOpenApiRules)
    }
}