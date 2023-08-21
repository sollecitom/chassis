package org.sollecitom.chassis.openapi.checking.checker

import io.swagger.v3.oas.models.OpenAPI
import java.net.URL

interface OpenApiComplianceChecker {

    fun check(openApiRaw: String): org.sollecitom.chassis.openapi.checking.checker.ComplianceCheckResult

    fun check(openApiLocation: URL): org.sollecitom.chassis.openapi.checking.checker.ComplianceCheckResult

    fun check(openApi: OpenAPI): org.sollecitom.chassis.openapi.checking.checker.ComplianceCheckResult

    companion object
}