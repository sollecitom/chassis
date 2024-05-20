package com.element.dpg.libs.chassis.openapi.validation.http4k.validator.validators

import com.atlassian.oai.validator.interaction.response.CustomResponseValidator
import com.atlassian.oai.validator.model.ApiOperation
import com.atlassian.oai.validator.model.Response
import com.atlassian.oai.validator.report.ValidationReport
import io.swagger.v3.oas.models.responses.ApiResponse
import org.http4k.core.ContentType
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.element.dpg.libs.chassis.json.utils.serde.Schema
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.kotlin.extensions.optional.asNullable
import com.element.dpg.libs.chassis.openapi.validation.http4k.validator.model.ResponseWithHeadersAdapter
import java.nio.charset.Charset
import io.swagger.v3.oas.models.media.Schema as SwaggerSchema

internal class ResponseJsonBodyValidator(val jsonSchemasDirectoryName: String = defaultJsonSchemasDirectory) : CustomResponseValidator {

    override fun validate(rawResponse: Response, apiOperation: ApiOperation): ValidationReport {

        val response = (rawResponse as ResponseWithHeadersAdapter)
        val bodyAsString = response.responseBody.asNullable()?.toString(Charset.defaultCharset())
        val responseDefinition = apiOperation.operation.responses[response.status.toString()]
        val bodySwaggerSchema = responseDefinition?.content?.get(response.acceptHeader.withNoDirectives().toHeaderValue())?.schema
        val bodySchema = bodySwaggerSchema?.`$ref`?.resolveAsSchemaLocation()?.let { jsonSchemaAt(it) }
        return when {
            !bodyAsString.isNullOrEmpty() && !bodySwaggerSchema.isDefined() && responseDefinition.declaresAJsonContentType() -> {
                bodySchema ?: return ValidationReport.singleton(ValidationReport.Message.create("Response body", "Present but JSON schema is not declared").build())
                val json = bodyAsString.toJsonValue()
                bodySchema.validate(json).toValidationReport()
            }

            else -> when {
                bodySchema != null -> ValidationReport.singleton(ValidationReport.Message.create("Response body", "Empty but JSON schema is declared").build())
                else -> ValidationReport.empty()
            }
        }
    }

    private fun SwaggerSchema<*>?.isDefined(): Boolean = this != null && properties.isNotEmpty()

    private fun Schema.ValidationFailure?.toValidationReport() = this?.let { ValidationReport.from(ValidationReport.Message.create("Response body", it.message).build()) } ?: ValidationReport.empty()

    private fun ApiResponse?.declaresAJsonContentType() = this?.content?.keys?.any { it == ContentType.APPLICATION_JSON.value } ?: false

    private fun String.resolveAsSchemaLocation(): String = when {
        startsWith("#/components/schemas/") -> "${removePrefix("#/components/schemas/")}.json"
        else -> removePrefix("./$jsonSchemasDirectoryName/")
    }

    private fun String.toJsonValue(): JsonValue = try {
        JSONObject(this).let(JsonValue::Object)
    } catch (e: JSONException) {
        JSONArray(this).let(JsonValue::Array)
    }

    private fun Schema.validate(json: JsonValue) = when (json) {
        is JsonValue.Array -> validate(json.value)
        is JsonValue.Object -> validate(json.value)
    }

    companion object {
        const val defaultJsonSchemasDirectory = "schemas/json"
    }
}

sealed class JsonValue(open val value: Any) {

    class Object(override val value: JSONObject) : JsonValue(value)
    class Array(override val value: JSONArray) : JsonValue(value)
}