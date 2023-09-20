package org.sollecitom.chassis.openapi.validation.http4k.validator.custom.validators

import com.atlassian.oai.validator.interaction.request.CustomRequestValidator
import com.atlassian.oai.validator.model.ApiOperation
import com.atlassian.oai.validator.model.Request
import com.atlassian.oai.validator.report.ValidationReport
import io.swagger.v3.oas.models.parameters.Parameter
import org.sollecitom.chassis.http4k.utils.lens.HttpHeaders
import org.sollecitom.chassis.openapi.validation.http4k.validator.utils.inHeader
import org.sollecitom.chassis.openapi.validation.http4k.validator.utils.parameters
import org.sollecitom.chassis.openapi.validation.request.validator.ValidationReportError

internal object UnknownHeadersRejectingRequestValidator : CustomRequestValidator {

    private val whitelistedUnknownHeaderNames = setOf(HttpHeaders.ContentType.name.lowercase())

    override fun validate(request: Request, apiOperation: ApiOperation): ValidationReport {

        val operationHeaders = apiOperation.parameters().inHeader().toSet()
        val unknownHeaderNames = request.headers.notDeclaredIn(operationHeaders)
        return if (unknownHeaderNames.isNotEmpty()) ValidationReport.from(unknownHeaderNames.map { ValidationReport.Message.create(ValidationReportError.Request.UnknownHeader.key, "Unknown request headers ${unknownHeaderNames.joinToString(separator = ",", prefix = "[", postfix = "]")}").build() }) else ValidationReport.empty()
    }

    private fun Map<String, Collection<String>>.notDeclaredIn(knownHeaders: Set<Parameter>): Set<String> {

        val knownHeaderNames = knownHeaders.map { it.name.lowercase() }.toSet()
        return keys.filterNot { it.lowercase() in whitelistedUnknownHeaderNames }.filterNot { headerName -> headerName.lowercase() in knownHeaderNames }.toSet()
    }
}