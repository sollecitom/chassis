package org.sollecitom.chassis.openapi.validation.http4k.validator.model

import com.atlassian.oai.validator.model.Response
import com.atlassian.oai.validator.model.SimpleResponse
import org.http4k.core.ContentType
import org.http4k.core.Headers
import org.sollecitom.chassis.openapi.validation.http4k.validator.utils.toMultiMap

internal class ResponseWithHeadersAdapter(private val delegate: SimpleResponse, http4kHeaders: Headers, override val acceptHeader: ContentType) : Response by delegate, ResponseWithHeaders {

    override val headers = http4kHeaders.toMultiMap()

    override fun getResponseBody() = delegate.responseBody
}
