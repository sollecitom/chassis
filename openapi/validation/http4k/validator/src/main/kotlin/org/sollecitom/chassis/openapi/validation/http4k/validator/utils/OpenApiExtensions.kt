package org.sollecitom.chassis.openapi.validation.http4k.validator.utils

import com.atlassian.oai.validator.model.ApiOperation
import io.swagger.v3.oas.models.parameters.HeaderParameter
import io.swagger.v3.oas.models.parameters.Parameter

internal fun ApiOperation.parameters(): List<Parameter> = operation.parameters ?: emptyList()

internal fun List<Parameter>.inHeader(): List<Parameter> = filterIsInstance<HeaderParameter>()