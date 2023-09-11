package org.sollecitom.chassis.example.write_endpoint.adapters.driving.web

import io.swagger.v3.oas.models.OpenAPI
import org.sollecitom.chassis.openapi.parser.OpenApiReader

private const val OPEN_API_FILE_LOCATION = "api/api.yaml"

val openApi: OpenAPI get() = OpenApiReader.parse(OPEN_API_FILE_LOCATION)