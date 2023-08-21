package org.sollecitom.chassis.openapi.checking.tests.sets

import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.media.StringSchema
import io.swagger.v3.oas.models.parameters.RequestBody
import org.sollecitom.chassis.openapi.checking.test.utils.content
import org.sollecitom.chassis.openapi.checking.test.utils.mediaTypes
import org.sollecitom.chassis.openapi.checking.test.utils.requestBody

interface OpenApiTestSpecification {

    val validPath: String
    val validOperationId: String
    val validSummary: String

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