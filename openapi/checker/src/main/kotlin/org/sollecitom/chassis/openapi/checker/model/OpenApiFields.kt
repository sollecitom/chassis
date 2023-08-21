package org.sollecitom.chassis.openapi.checker.model

import io.swagger.v3.oas.models.Operation as ApiOperation

object OpenApiFields {

    object Operation {

        val summary: OpenApiField<ApiOperation, String> by lazy { OpenApiField("summary", ApiOperation::getSummary) }
        val description: OpenApiField<ApiOperation, String> by lazy { OpenApiField("description", ApiOperation::getDescription) }
    }
}