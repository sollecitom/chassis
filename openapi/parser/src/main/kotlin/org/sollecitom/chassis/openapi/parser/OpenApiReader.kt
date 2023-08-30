package org.sollecitom.chassis.openapi.parser

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.parser.OpenAPIV3Parser
import io.swagger.v3.parser.core.models.ParseOptions
import io.swagger.v3.parser.core.models.SwaggerParseResult
import org.sollecitom.chassis.openapi.parser.OpenApiValidator.Result.Invalid
import org.sollecitom.chassis.openapi.parser.OpenApiValidator.Result.Valid

object OpenApiReader : OpenApiValidator, OpenApiParser {

    override fun validate(openApiLocation: String) = loadOpenApi(openApiLocation).asValidatorResult()

    override fun parseContent(openApi: String): OpenAPI = readOpenApi(openApi).getOrThrow()

    override fun parse(openApiLocation: String): OpenAPI = loadOpenApi(openApiLocation).getOrThrow()

    private fun loadOpenApi(openApiLocation: String): SwaggerParseResult {

        val parseOptions = ParseOptions()
        parseOptions.isResolve = true
        parseOptions.isResolveFully = true
        parseOptions.isResolveRequestBody = true
        parseOptions.isResolveCombinators = true
        return OpenAPIV3Parser().readLocation(openApiLocation, emptyList(), parseOptions)
    }

    private fun SwaggerParseResult.asValidatorResult(): OpenApiValidator.Result = if (messages.isEmpty()) Valid else Invalid(messages.map(OpenApiValidator::Error).toSet())

    private fun readOpenApi(openApi: String) = OpenAPIV3Parser().readContents(openApi)

    private fun SwaggerParseResult.getOrThrow(): OpenAPI = takeIf { messages.isEmpty() }?.openAPI ?: throw OpenApiParser.ParseException(messages)
}

