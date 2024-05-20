package com.element.dpg.libs.chassis.openapi.parser

import io.swagger.v3.oas.models.OpenAPI
import java.net.URL
import kotlin.io.path.toPath

interface OpenApiParser {

    fun parseContent(openApi: String): OpenAPI

    fun parse(openApiLocation: String): OpenAPI

    class ParseException(val messages: List<String>) : RuntimeException(messages.joinToString(System.lineSeparator()))
}

fun OpenApiParser.parse(validOpenApiUrl: URL) = parse(validOpenApiUrl.toURI().toPath().toString())