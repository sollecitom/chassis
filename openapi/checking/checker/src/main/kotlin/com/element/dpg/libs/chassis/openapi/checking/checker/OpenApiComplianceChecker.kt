package com.element.dpg.libs.chassis.openapi.checking.checker

import io.swagger.v3.oas.models.OpenAPI
import java.net.URL

interface OpenApiComplianceChecker {

    fun check(openApiRaw: String): com.element.dpg.libs.chassis.openapi.checking.checker.ComplianceCheckResult

    fun check(openApiLocation: URL): com.element.dpg.libs.chassis.openapi.checking.checker.ComplianceCheckResult

    fun check(openApi: OpenAPI): com.element.dpg.libs.chassis.openapi.checking.checker.ComplianceCheckResult

    companion object
}