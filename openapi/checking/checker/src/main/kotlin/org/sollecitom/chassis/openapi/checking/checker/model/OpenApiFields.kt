package org.sollecitom.chassis.openapi.checking.checker.model

import io.swagger.v3.oas.models.Operation as ApiOperation
import io.swagger.v3.oas.models.info.Info as OpenApiInfo

object OpenApiFields {

    object Info {
        val title: OpenApiField<OpenApiInfo, String> by lazy { OpenApiField("title") { info -> info.title } }
        val description: OpenApiField<OpenApiInfo, String> by lazy { OpenApiField("description") { info -> info.description } }
        val version: OpenApiField<OpenApiInfo, String> by lazy { OpenApiField("version") { info -> info.version } }
    }

    object Operation {

        val summary: OpenApiField<ApiOperation, String> by lazy { OpenApiField("summary", ApiOperation::getSummary) }
        val description: OpenApiField<ApiOperation, String> by lazy { OpenApiField("description", ApiOperation::getDescription) }
    }
}