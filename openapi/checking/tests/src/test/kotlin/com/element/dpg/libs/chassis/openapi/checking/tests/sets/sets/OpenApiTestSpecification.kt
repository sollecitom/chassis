package com.element.dpg.libs.chassis.openapi.checking.tests.sets.sets

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.media.StringSchema
import io.swagger.v3.oas.models.parameters.RequestBody
import com.element.dpg.libs.chassis.openapi.builder.OpenApiBuilder
import com.element.dpg.libs.chassis.openapi.builder.content
import com.element.dpg.libs.chassis.openapi.builder.mediaTypes
import com.element.dpg.libs.chassis.openapi.builder.requestBody

interface OpenApiTestSpecification {

    val validPath: String
    val validOperationId: String
    val validSummary: String

    fun openApi(version: OpenApiBuilder.OpenApiVersion = OpenApiBuilder.OpenApiVersion.V3_1_0, customize: OpenApiBuilder.() -> Unit): OpenAPI

    fun Operation.withValidFields() {

        operationId = validOperationId
        summary = validSummary
    }

    fun Operation.setValidRequestBody() {

        requestBody {
            withValidFields()
        }
    }

    fun RequestBody.withValidFields() {

        required = true
        description = "Some request body description."
        content {
            mediaTypes {
                add("text/plain") {
                    schema = StringSchema()
                    example = "Some text"
                }
            }
        }
    }
}