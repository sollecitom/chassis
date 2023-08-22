package org.sollecitom.chassis.openapi.validation.http4k.validator

import com.atlassian.oai.validator.interaction.request.CustomRequestValidator
import com.atlassian.oai.validator.model.ApiOperation
import com.atlassian.oai.validator.model.Request
import com.atlassian.oai.validator.report.ValidationReport
import io.swagger.v3.oas.models.parameters.HeaderParameter
import io.swagger.v3.oas.models.parameters.Parameter

// TODO create similar ones for query params, etc.?
internal object UnknownHeadersRejectingRequestValidator : CustomRequestValidator {

    override fun validate(request: Request, apiOperation: ApiOperation): ValidationReport {

        val operationHeaders = apiOperation.parameters().inHeader().toSet()
        val unknownHeaderNames = request.headers.notDeclaredIn(operationHeaders)
        return if (unknownHeaderNames.isNotEmpty()) ValidationReport.from(unknownHeaderNames.map { ValidationReport.Message.create(it, "Unknown header parameter").build() }) else ValidationReport.empty()
    }

    private fun ApiOperation.parameters(): List<Parameter> = operation.parameters ?: emptyList()

    // TODO fix
    private fun List<Parameter>.inHeader(): List<Parameter> = filterIsInstance<HeaderParameter>()
//    private fun List<Parameter>.inHeader(): List<Parameter> = filter { it.`in` == "header" }

    private fun Map<String, Collection<String>>.notDeclaredIn(knownHeaders: Set<Parameter>): Set<String> {

        val knownHeaderNames = knownHeaders.map { it.name.lowercase() }.toSet()
        // TODO externalize this "content-type"
        return keys.filterNot { it.lowercase() == "content-type" }.filterNot { headerName -> headerName.lowercase() in knownHeaderNames }.toSet() // TODO do we really need to exclude content-type here? make this an option?
    }
}