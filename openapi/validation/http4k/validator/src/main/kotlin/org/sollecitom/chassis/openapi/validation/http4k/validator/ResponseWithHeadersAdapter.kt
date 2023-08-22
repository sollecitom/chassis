package org.sollecitom.chassis.openapi.validation.http4k.validator

import com.atlassian.oai.validator.model.Response
import com.atlassian.oai.validator.model.SimpleResponse
import org.http4k.core.Headers
import org.http4k.core.toParametersMap

internal class ResponseWithHeadersAdapter(val delegate: SimpleResponse, http4kHeaders: Headers, override val acceptHeader: String) : Response by delegate, ResponseWithHeaders {

    override val headers = http4kHeaders.toMultiMap()

    override fun getResponseBody() = delegate.responseBody
}

// TODO move
fun Headers.toMultiMap(): Map<String, Collection<String>> = toParametersMap().mapValues { it.value.filterNotNull() }
