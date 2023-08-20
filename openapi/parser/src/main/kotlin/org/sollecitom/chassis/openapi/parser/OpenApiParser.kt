package org.sollecitom.chassis.openapi.parser

import io.swagger.v3.oas.models.OpenAPI
import java.net.URL

interface OpenApiParser {

    fun parseContent(openApi: String): OpenAPI

    fun parse(openApiLocation: String): OpenAPI

    class ParseException(val messages: List<String>) : RuntimeException(messages.joinToString(System.lineSeparator()))
}

fun OpenApiParser.parse(validOpenApiUrl: URL) = parse(validOpenApiUrl.toString())