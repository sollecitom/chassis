package org.sollecitom.chassis.openapi.validation.http4k.validator

import com.atlassian.oai.validator.OpenApiInteractionValidator
import com.atlassian.oai.validator.model.SimpleRequest
import com.atlassian.oai.validator.model.SimpleResponse
import com.atlassian.oai.validator.report.ValidationReport
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.parser.core.models.ParseOptions
import org.http4k.core.*
import org.sollecitom.chassis.http4k.utils.lens.contentType
import org.sollecitom.chassis.openapi.parser.OpenApi
import java.nio.ByteBuffer
import com.atlassian.oai.validator.model.Request as OpenApiRequest

// TODO target an OpenApi file instead (move resolution outside of this module in terms of responsibility?)
class StandardHttp4KOpenApiValidator(openApi: OpenAPI, rejectUnknownRequestParameters: Boolean, rejectUnknownResponseHeaders: Boolean, jsonSchemasDirectoryName: String = ResponseJsonBodyValidator.defaultJsonSchemasDirectory) : Http4kOpenApiValidator {

    init {
        OpenApi.enableVersion310OrHigher() // TODO check if it works without and remove it
    }

    private val responseJsonBodyValidator = ResponseJsonBodyValidator(jsonSchemasDirectoryName = jsonSchemasDirectoryName)
    private val requestValidator: OpenApiInteractionValidator = OpenApiInteractionValidator.createFor(openApi).withRejectUnknownRequestHeaders(rejectUnknownRequestParameters).build()
    private val responseValidator: OpenApiInteractionValidator = OpenApiInteractionValidator.createFor(openApi).withParseOptions(ParseOptions().apply {
        isResolve = true
        isFlattenComposedSchemas = false
        isResolveFully = false
    }).withRejectUnknownResponseHeaders(rejectUnknownResponseHeaders).withCustomResponseValidation(responseJsonBodyValidator).build()

    override fun validate(request: Request) {

        val validationReport = requestValidator.validateRequest(request.adapted())
        return validationReport.orThrowValidationException()
    }

    override fun validate(path: String, method: Method, acceptHeader: String, response: Response) {

        val validationReport = responseValidator.validateResponse(path, method.adapted(), response.adapted(acceptHeader))
        return validationReport.orThrowValidationException()
    }

    private fun ValidationReport.orThrowValidationException() { // TODO return the report instead?
        if (messages.isNotEmpty()) throw Http4kOpenApiValidationException(messages.map { "${it.key}: ${it.message}" })
    }

    private fun Response.adapted(acceptHeader: String): ResponseWithHeaders = SimpleResponse.Builder.status(status.code).apply {
        withBody(body)
        headers.toMultiMap().forEach { header -> withHeader(header.key, header.value.toList()) }
    }.build().let { ResponseWithHeadersAdapter(it, headers, acceptHeader) }

    private fun Method.adapted(): OpenApiRequest.Method = OpenApiRequest.Method.valueOf(name)

    private fun ByteBuffer.toByteArray(): ByteArray { // TODO check

        val byteArray = ByteArray(capacity())
        get(byteArray)
        return byteArray
    }

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
        }.invoke(uri.path) // url.encodedPath

        with(builder) {
            withBody(body)
            contentType?.let { contentType -> builder.withContentType(contentType.toHeaderValue()) } // TODO try withNoDirectives() if it doesn't work
            headers.toMultiMap().forEach { header -> withHeader(header.key, header.value.toList()) }
        }
        return builder.build()
    }

    private fun OpenApiInteractionValidator.Builder.withRejectUnknownRequestHeaders(rejectUnknownParameters: Boolean): OpenApiInteractionValidator.Builder = when (rejectUnknownParameters) {
        true -> withCustomRequestValidation(UnknownHeadersRejectingRequestValidator)
        else -> this
    }

    private fun OpenApiInteractionValidator.Builder.withRejectUnknownResponseHeaders(rejectUnknownResponseHeaders: Boolean): OpenApiInteractionValidator.Builder = when (rejectUnknownResponseHeaders) {
        true -> withCustomResponseValidation(UnknownHeadersRejectingResponseValidator)
        else -> this
    }

    private fun SimpleRequest.Builder.withBody(body: Body) {
        when (body) {
            Body.EMPTY -> { /* nothing to do here */
            }
            // TODO treat the StreamBody differently?
            else -> withBody(body.payload.toByteArray())
        }
    }

    private fun SimpleResponse.Builder.withBody(body: Body) {
        when (body) {
            Body.EMPTY -> { /* nothing to do here */
            }
            // TODO treat the StreamBody differently?
            else -> withBody(body.payload.toByteArray())
        }
    }
}