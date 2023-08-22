package org.sollecitom.chassis.openapi.validation.http4k.test.utils

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isSuccess
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.sollecitom.chassis.http4k.utils.lens.contentType
import org.sollecitom.chassis.openapi.validation.http4k.validator.Http4kOpenApiValidator

// TODO add tests
interface WithHttp4kOpenApiValidationSupport {

    val openApiValidator: Http4kOpenApiValidator

    suspend fun Response.ensureCompliantWith(path: String, method: Method, contentType: ContentType) {

        val result = runCatching { openApiValidator.validate(path, method, contentType.toString(), this) }
        assertThat(result).isSuccess()
    }

    suspend fun Assert<Response>.compliesWithOpenApi(path: String, method: Method, contentType: ContentType) = given { response -> response.ensureCompliantWith(path, method, contentType) }

    suspend fun Assert<Response>.compliesWithOpenApiForRequest(request: Request) = compliesWithOpenApi(request.uri.path, request.method, request.contentType!!)

    fun Request.ensureCompliantWithOpenApi(): Request {

        openApiValidator.validate(this)
        return this
    }

    fun Request.ensureNonCompliantWithOpenApi(): Request {

        val result = runCatching { ensureCompliantWithOpenApi() }
        if (result.isSuccess) {
            error("Expected for the request NOT to comply with Swagger, but it did!")
        }
        return this
    }
}