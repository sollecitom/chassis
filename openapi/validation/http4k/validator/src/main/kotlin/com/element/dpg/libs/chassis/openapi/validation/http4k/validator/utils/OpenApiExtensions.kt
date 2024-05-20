package com.element.dpg.libs.chassis.openapi.validation.http4k.validator.utils

import com.atlassian.oai.validator.model.ApiOperation
import io.swagger.v3.oas.models.parameters.HeaderParameter
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.parameters.QueryParameter

internal fun ApiOperation.parameters(): List<Parameter> = operation.parameters ?: emptyList()

internal fun List<Parameter>.inHeader(): List<Parameter> = filterIsInstance<HeaderParameter>()

internal fun List<Parameter>.inQuery(): List<Parameter> = filterIsInstance<QueryParameter>()