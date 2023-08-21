package org.sollecitom.chassis.openapi.checker

import io.swagger.v3.oas.models.OpenAPI
import java.net.URL

interface OpenApiComplianceChecker {

    fun check(openApiRaw: String): ComplianceCheckResult

    fun check(openApiLocation: URL): ComplianceCheckResult

    fun check(openApi: OpenAPI): ComplianceCheckResult

    companion object
}