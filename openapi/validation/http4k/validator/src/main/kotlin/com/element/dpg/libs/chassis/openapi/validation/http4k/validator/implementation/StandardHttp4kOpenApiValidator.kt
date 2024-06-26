package com.element.dpg.libs.chassis.openapi.validation.http4k.validator.implementation

import com.atlassian.oai.validator.OpenApiInteractionValidator
import com.atlassian.oai.validator.model.SimpleRequest
import com.atlassian.oai.validator.model.SimpleResponse
import com.atlassian.oai.validator.report.ValidationReport
import com.element.dpg.libs.chassis.http4k.utils.lens.contentType
import com.element.dpg.libs.chassis.openapi.parser.OpenApi
import com.element.dpg.libs.chassis.openapi.validation.http4k.validator.Http4kOpenApiValidator
import com.element.dpg.libs.chassis.openapi.validation.http4k.validator.model.ResponseWithHeaders
import com.element.dpg.libs.chassis.openapi.validation.http4k.validator.model.ResponseWithHeadersAdapter
import com.element.dpg.libs.chassis.openapi.validation.http4k.validator.utils.toMultiMap
import com.element.dpg.libs.chassis.openapi.validation.http4k.validator.validators.ResponseJsonBodyValidator
import com.element.dpg.libs.chassis.openapi.validation.http4k.validator.validators.UnknownHeadersRejectingRequestValidator
import com.element.dpg.libs.chassis.openapi.validation.http4k.validator.validators.UnknownHeadersRejectingResponseValidator
import com.element.dpg.libs.chassis.openapi.validation.http4k.validator.validators.UnknownQueryParamsRejectingRequestValidator
import io.swagger.v3.oas.models.OpenAPI
import org.http4k.core.*
import com.atlassian.oai.validator.model.Request as OpenApiRequest

operator fun Http4kOpenApiValidator.Companion.invoke(openApi: OpenAPI, rejectUnknownRequestParameters: Boolean = true, rejectUnknownResponseHeaders: Boolean = true, jsonSchemasDirectoryName: String = ResponseJsonBodyValidator.defaultJsonSchemasDirectory): Http4kOpenApiValidator = StandardHttp4kOpenApiValidator(openApi, rejectUnknownRequestParameters, rejectUnknownResponseHeaders, jsonSchemasDirectoryName)

internal class StandardHttp4kOpenApiValidator(openApi: OpenAPI, rejectUnknownRequestParameters: Boolean = true, rejectUnknownResponseHeaders: Boolean = true, jsonSchemasDirectoryName: String = ResponseJsonBodyValidator.defaultJsonSchemasDirectory) : Http4kOpenApiValidator {

    init {
        OpenApi.bindMultipleTypesToASingleType()
    }

    private val requestValidator: OpenApiInteractionValidator = OpenApiInteractionValidator.createFor(openApi).withRejectUnknownRequestHeaders(rejectUnknownRequestParameters).build()
    private val responseJsonBodyValidator = ResponseJsonBodyValidator(jsonSchemasDirectoryName = jsonSchemasDirectoryName)
    private val responseValidator: OpenApiInteractionValidator = OpenApiInteractionValidator.createFor(openApi).withRejectUnknownResponseHeaders(rejectUnknownResponseHeaders).withCustomResponseValidation(responseJsonBodyValidator).build()

    override fun validate(request: Request): ValidationReport {
        return requestValidator.validateRequest(request.adapted())
    }

    override fun validate(path: String, method: Method, acceptHeader: ContentType, response: Response) = responseValidator.validateResponse(path, method.adapted(), response.adapted(acceptHeader))

    private fun Response.adapted(acceptHeader: ContentType): ResponseWithHeaders = SimpleResponse.Builder.status(status.code).apply {
        withBody(body)
        headers.toMultiMap().forEach { header -> withHeader(header.key, header.value.toList()) }
    }.build().let { ResponseWithHeadersAdapter(it, headers, acceptHeader) }

    private fun Method.adapted(): OpenApiRequest.Method = OpenApiRequest.Method.valueOf(name)

    private fun Request.adapted(): SimpleRequest {

        val builder = when (method) {
            Method.GET -> SimpleRequest.Builder::get
            Method.POST -> SimpleRequest.Builder::post
            Method.PUT -> SimpleRequest.Builder::put
            Method.DELETE -> SimpleRequest.Builder::delete
            Method.OPTIONS -> SimpleRequest.Builder::options
            Method.TRACE -> SimpleRequest.Builder::trace
            Method.PATCH -> SimpleRequest.Builder::patch
            Method.PURGE -> { path -> SimpleRequest.Builder("purge", path) }
            Method.HEAD -> SimpleRequest.Builder::head
        }.invoke(uri.path)

        with(builder) {
            withBody(body)
            contentType?.let { contentType -> builder.withContentType(contentType.toHeaderValue()) }
            headers.toMultiMap().forEach { header -> withHeader(header.key, header.value.toList()) }
            uri.queries().toMultiMap().forEach { query -> withQueryParam(query.key, query.value.toList()) }
        }
        return builder.build()
    }

    private fun OpenApiInteractionValidator.Builder.withRejectUnknownRequestHeaders(rejectUnknownParameters: Boolean): OpenApiInteractionValidator.Builder = when (rejectUnknownParameters) {
        true -> withCustomRequestValidation(UnknownHeadersRejectingRequestValidator).withCustomRequestValidation(UnknownQueryParamsRejectingRequestValidator)
        else -> this
    }

    private fun OpenApiInteractionValidator.Builder.withRejectUnknownResponseHeaders(rejectUnknownResponseHeaders: Boolean): OpenApiInteractionValidator.Builder = when (rejectUnknownResponseHeaders) {
        true -> withCustomResponseValidation(UnknownHeadersRejectingResponseValidator)
        else -> this
    }

    private fun SimpleRequest.Builder.withBody(body: Body): SimpleRequest.Builder = when (body) {
        is MemoryBody -> withBody(body.payload.array())
        is StreamBody -> withBody(body.stream)
        else -> error("Unknown body type $body")
    }

    private fun SimpleResponse.Builder.withBody(body: Body): SimpleResponse.Builder = when (body) {
        is MemoryBody -> withBody(body.payload.array())
        is StreamBody -> withBody(body.stream)
        else -> error("Unknown body type $body")
    }
}