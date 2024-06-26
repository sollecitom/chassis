package com.element.dpg.libs.chassis.openapi.validation.http4k.test.utils

import assertk.Assert
import assertk.assertThat
import com.element.dpg.libs.chassis.http4k.utils.lens.contentType
import com.element.dpg.libs.chassis.openapi.validation.http4k.validator.Http4kOpenApiValidator
import com.element.dpg.libs.chassis.openapi.validation.request.validator.ValidationReportError
import com.element.dpg.libs.chassis.openapi.validation.request.validator.test.utils.containsOnly
import com.element.dpg.libs.chassis.openapi.validation.request.validator.test.utils.hasErrors
import com.element.dpg.libs.chassis.openapi.validation.request.validator.test.utils.hasNoErrors
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response

interface WithHttp4kOpenApiValidationSupport {

    val openApiValidator: Http4kOpenApiValidator

    fun Response.ensureCompliantWith(path: String, method: Method, contentType: ContentType, printErrors: Boolean = true, errorsCount: Int? = null) {

        val report = openApiValidator.validate(path, method, contentType, this)
        assertThat(report).hasNoErrors(printErrors = printErrors)
    }

    fun Assert<Response>.compliesWithOpenApi(path: String, method: Method, contentType: ContentType) = given { response -> response.ensureCompliantWith(path, method, contentType) }

    fun Assert<Response>.compliesWithOpenApiForRequest(request: Request) = compliesWithOpenApi(request.uri.path, request.method, request.contentType!!)

    fun Assert<Response>.doesNotComplyWithOpenApiForRequest(request: Request, error: ValidationReportError, printErrors: Boolean = false) = given { response ->

        val report = openApiValidator.validate(request.uri.path, request.method, request.contentType!!, response)
        assertThat(report).containsOnly(error = error, printErrors = printErrors)
    }

    fun Request.ensureCompliantWithOpenApi(printErrors: Boolean = true): Request {

        val report = openApiValidator.validate(this)
        assertThat(report).hasNoErrors(printErrors = printErrors)
        return this
    }

    fun Request.ensureNonCompliantWithOpenApi(printErrors: Boolean = true, errorsCount: Int? = null): Request {

        val report = openApiValidator.validate(this)
        assertThat(report).hasErrors(printErrors = printErrors, count = errorsCount)
        return this
    }

    fun Request.ensureNonCompliantWithOpenApi(error: ValidationReportError.Request, printErrors: Boolean = false): Request {

        val report = openApiValidator.validate(this)
        assertThat(report).containsOnly(error = error, printErrors = printErrors)
        return this
    }
}