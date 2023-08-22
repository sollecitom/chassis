package org.sollecitom.chassis.openapi.validation.http4k.validator

import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response

interface Http4kOpenApiValidator {

    fun validate(request: Request)

    fun validate(path: String, method: Method, acceptHeader: String, response: Response)

    companion object
}

fun Http4kOpenApiValidator.validate(path: String, method: Method, acceptHeader: ContentType, response: Response) = validate(path, method, acceptHeader.toHeaderValue(), response)