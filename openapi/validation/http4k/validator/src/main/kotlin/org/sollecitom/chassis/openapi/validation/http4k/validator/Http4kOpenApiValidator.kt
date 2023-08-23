package org.sollecitom.chassis.openapi.validation.http4k.validator

import com.atlassian.oai.validator.report.ValidationReport
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response

interface Http4kOpenApiValidator {

    fun validate(request: Request): ValidationReport

    fun validate(path: String, method: Method, acceptHeader: ContentType, response: Response): ValidationReport

    companion object
}