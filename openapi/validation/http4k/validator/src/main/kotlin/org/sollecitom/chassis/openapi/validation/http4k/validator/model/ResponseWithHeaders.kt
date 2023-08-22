package org.sollecitom.chassis.openapi.validation.http4k.validator.model

import com.atlassian.oai.validator.model.Response

internal interface ResponseWithHeaders : Response {

    val acceptHeader: String
    val headers: Map<String, Collection<String>>
}