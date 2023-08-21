package org.sollecitom.chassis.openapi.checker.sets

import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem

interface WithMethodContext : OpenApiTestSpecification {

    val method: PathItem.HttpMethod

    fun Operation.withValidRequestBody()
}