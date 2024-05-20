package com.element.dpg.libs.chassis.openapi.validation.http4k.validator.validators

import com.atlassian.oai.validator.interaction.response.CustomResponseValidator
import com.atlassian.oai.validator.model.ApiOperation
import com.atlassian.oai.validator.model.Response
import com.atlassian.oai.validator.report.ValidationReport
import com.element.dpg.libs.chassis.openapi.validation.http4k.validator.model.ResponseWithHeadersAdapter
import com.element.dpg.libs.chassis.openapi.validation.request.validator.ValidationReportError

internal object UnknownHeadersRejectingResponseValidator : CustomResponseValidator {

    override fun validate(response: Response, apiOperation: ApiOperation): ValidationReport {

        val responseHeaders = apiOperation.operation.responses[response.status.toString()]?.headers ?: emptyMap()
        val responseHeaderNames = responseHeaders.map { it.key.lowercase() }.toSet()
        val unknownHeaders = (response as ResponseWithHeadersAdapter).headers.filterNot { header -> header.key.lowercase() in responseHeaderNames }.map(Map.Entry<String, Collection<String>>::key)
        return if (unknownHeaders.isNotEmpty()) ValidationReport.from(unknownHeaders.map { ValidationReport.Message.create(ValidationReportError.Response.UnknownHeader.key, "Unknown response headers ${unknownHeaders.joinToString(separator = ",", prefix = "[", postfix = "]")}").build() }) else ValidationReport.empty()
    }
}