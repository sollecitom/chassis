package org.sollecitom.chassis.openapi.validation.http4k.validator

import com.atlassian.oai.validator.interaction.response.CustomResponseValidator
import com.atlassian.oai.validator.model.ApiOperation
import com.atlassian.oai.validator.model.Response
import com.atlassian.oai.validator.report.ValidationReport
import com.github.erosb.jsonsKema.Schema
import com.github.erosb.jsonsKema.ValidationFailure
import io.swagger.v3.oas.models.responses.ApiResponse
import org.http4k.core.ContentType
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.sollecitom.chassis.json.utils.jsonSchemaUnderRootFolder
import org.sollecitom.chassis.json.utils.validate
import org.sollecitom.chassis.kotlin.extensions.optional.asNullable
import java.nio.charset.Charset

internal class ResponseJsonBodyValidator(val jsonSchemasDirectoryName: String = defaultJsonSchemasDirectory, val rootApiResourcesDirectoryName: String = defaultApiResourcesDirectory) : CustomResponseValidator {

    private val jsonSchemaDirectory = "$rootApiResourcesDirectoryName/$jsonSchemasDirectoryName"

    override fun validate(rawResponse: Response, apiOperation: ApiOperation): ValidationReport { // TODO refactor it and fix it

        val response = (rawResponse as ResponseWithHeadersAdapter)
        val bodyAsString = response.responseBody.asNullable()?.toString(Charset.defaultCharset())
        val responseDefinition = apiOperation.operation.responses[response.status.toString()]
        val bodySwaggerSchema = responseDefinition?.content?.get(response.acceptHeader)?.schema
        val bodySchema = bodySwaggerSchema?.`$ref`?.resolveAsSchemaLocation()?.let { jsonSchemaUnderRootFolder(it, jsonSchemaDirectory) } // TODO replace with simple load if this works
        return when {
            !bodyAsString.isNullOrEmpty() && responseDefinition.declaresAJsonContentType() -> {
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

    private fun ValidationFailure?.toValidationReport() = this?.let { ValidationReport.from(ValidationReport.Message.create("Response body", it.toString()).build()) } ?: ValidationReport.empty()

    private fun ApiResponse?.declaresAJsonContentType() = this?.content?.keys?.any { it == ContentType.APPLICATION_JSON.toString() } ?: true // TODO check

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
        const val defaultApiResourcesDirectory = "api"
    }
}

sealed class JsonValue(open val value: Any) {

    class Object(override val value: JSONObject) : JsonValue(value)
    class Array(override val value: JSONArray) : JsonValue(value)
}