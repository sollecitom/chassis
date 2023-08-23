package org.sollecitom.chassis.openapi.validation.http4k.validator.model

import com.atlassian.oai.validator.model.Response
import org.http4k.core.ContentType

internal interface ResponseWithHeaders : Response {

    val acceptHeader: ContentType
    val headers: Map<String, Collection<String>>
}