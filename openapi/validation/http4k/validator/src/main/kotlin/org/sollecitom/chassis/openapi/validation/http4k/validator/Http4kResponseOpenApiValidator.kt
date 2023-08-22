package org.sollecitom.chassis.openapi.validation.http4k.validator

import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.lens.Header

interface Http4kResponseOpenApiValidator {

    fun validate(path: String, method: Method, acceptHeader: String, response: Response)
}

// TODO check
fun Http4kResponseOpenApiValidator.validate(path: String, method: Method, acceptHeader: ContentType, response: Response) = validate(path, method, acceptHeader.toHeaderValue(), response)

fun Http4kResponseOpenApiValidator.validate(response: Response, request: Request, acceptHeader: String) = validate(request.uri.path, request.method, acceptHeader, response)