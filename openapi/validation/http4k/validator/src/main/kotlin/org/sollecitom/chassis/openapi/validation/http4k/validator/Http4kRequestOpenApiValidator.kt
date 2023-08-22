package org.sollecitom.chassis.openapi.validation.http4k.validator

import org.http4k.core.Request

interface Http4kRequestOpenApiValidator {

    fun validate(request: Request)
}