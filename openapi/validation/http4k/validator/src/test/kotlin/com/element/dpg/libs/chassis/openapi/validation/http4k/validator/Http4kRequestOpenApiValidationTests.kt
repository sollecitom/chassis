package com.element.dpg.libs.chassis.openapi.validation.http4k.validator

import assertk.assertThat
import org.http4k.core.*
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.http4k.utils.lens.body
import org.sollecitom.chassis.logger.core.loggable.Loggable
import com.element.dpg.libs.chassis.openapi.parser.OpenApiReader
import com.element.dpg.libs.chassis.openapi.validation.http4k.validator.implementation.invoke
import com.element.dpg.libs.chassis.openapi.validation.request.validator.ValidationReportError
import com.element.dpg.libs.chassis.openapi.validation.request.validator.test.utils.containsOnly
import com.element.dpg.libs.chassis.openapi.validation.request.validator.test.utils.hasNoErrors

@TestInstance(PER_CLASS)
private class Http4kRequestOpenApiValidationTests {

    private val openApi = OpenApiReader.parse(API_FILE_LOCATION)
    private val validator = Http4kOpenApiValidator(openApi = openApi)

    @Nested
    inner class Requests {

        private val requiredRequestHeaderName = "X-A-Request-Header"
        private fun validRequest(json: JSONObject, path: String = PATH) = Request(Method.POST, uri(path)).body(json).header(requiredRequestHeaderName, "value")
        private fun validRequest(json: JSONArray, path: String = PATH) = Request(Method.POST, uri(path)).body(json).header(requiredRequestHeaderName, "value")

        @Test
        fun `parsing the OpenAPI file`() {

            val api = OpenApiReader.parse(API_FILE_LOCATION)
            println(api)
        }

        @Test
        fun `are confirmed valid when valid`() {

            val json = validPersonDetails.toJson()
            val request = validRequest(json)

            val report = validator.validate(request)

            assertThat(report).hasNoErrors()
        }

        @Test
        fun `are rejected as invalid when a required header is missing`() {

            val json = validPersonDetails.toJson()
            val request = validRequest(json).removeHeader(requiredRequestHeaderName)

            val report = validator.validate(request)

            assertThat(report).containsOnly(ValidationReportError.Request.MissingRequiredHeader)
        }

        @Test
        fun `are rejected as invalid when an unknown header is specified`() {

            val json = validPersonDetails.toJson()
            val request = validRequest(json).body(json).header("Unknown-Header", "unknown header value")

            val report = validator.validate(request)

            assertThat(report).containsOnly(ValidationReportError.Request.UnknownHeader)
        }

        @Test
        fun `are rejected as invalid when an unknown query param is specified`() {

            val json = validPersonDetails.toJson()
            val request = validRequest(json).body(json).query("unknown-query-param", "unknown-query-param-value")

            val report = validator.validate(request)

            assertThat(report).containsOnly(ValidationReportError.Request.UnknownHeader)
        }

        @Test
        fun `are rejected as invalid when the body is invalid`() {

            val invalidJson = validPersonDetails.toJson().apply { remove("firstName") }
            val request = validRequest(invalidJson).body(invalidJson)

            val report = validator.validate(request)

            assertThat(report).containsOnly(ValidationReportError.Request.Body.MissingRequiredField)
        }

        @Test
        fun `are rejected as invalid when the body is invalid JSON type`() {


            val json = sequenceOf(validPersonDetails.toJson(), validPersonDetails.toJson()).fold(JSONArray(), JSONArray::put)
            val request = validRequest(json)

            val report = validator.validate(request)

            assertThat(report).containsOnly(ValidationReportError.Request.Body.InvalidType)
        }
    }

    @Nested
    inner class Responses {

        private val requiredResponseHeaderName = "X-A-Response-Header"
        private fun validResponse(json: JSONObject, status: Status = Status.ACCEPTED) = Response(status).body(json).header(requiredResponseHeaderName, "value")
        private fun validResponse(json: JSONArray, status: Status = Status.ACCEPTED) = Response(status).body(json).header(requiredResponseHeaderName, "value")

        @Test
        fun `are confirmed valid when valid`() {

            val json = validPersonDetails.toJson()
            val response = validResponse(json)

            val report = validator.validate(PATH, Method.POST, defaultAcceptHeader, response)

            assertThat(report).hasNoErrors()
        }

        @Test
        fun `are rejected as invalid when a required header is missing`() {


            val json = validPersonDetails.toJson()
            val response = validResponse(json).removeHeader(requiredResponseHeaderName)

            val report = validator.validate(PATH, Method.POST, defaultAcceptHeader, response)

            assertThat(report).containsOnly(ValidationReportError.Response.MissingRequiredHeader)
        }

        @Test
        fun `are rejected as invalid when an unknown header is specified`() {


            val json = validPersonDetails.toJson()
            val response = validResponse(json).header("Unknown-Response-Header", "value")

            val report = validator.validate(PATH, Method.POST, defaultAcceptHeader, response)

            assertThat(report).containsOnly(ValidationReportError.Response.UnknownHeader)
        }

        @Test
        fun `are rejected as invalid when the body is invalid`() {

            val invalidJson = validPersonDetails.toJson().apply { remove("firstName") }
            val response = validResponse(invalidJson)

            val report = validator.validate(PATH, Method.POST, defaultAcceptHeader, response)

            assertThat(report).containsOnly(ValidationReportError.Response.Body.MissingRequiredField)
        }

        @Test
        fun `are rejected as invalid when the body is invalid JSON type`() {


            val json = sequenceOf(validPersonDetails.toJson(), validPersonDetails.toJson()).fold(JSONArray(), JSONArray::put)
            val response = validResponse(json)

            val report = validator.validate(PATH, Method.POST, defaultAcceptHeader, response)

            assertThat(report).containsOnly(ValidationReportError.Response.Body.InvalidType)
        }

        @Test
        fun `are confirmed valid when valid with nested objects`() {

            val json = JSONObject("""{"errors":[{"message":"First name cannot be equal to last name"}]}""")
            val response = validResponse(json = json, status = Status.UNPROCESSABLE_ENTITY)

            val report = validator.validate(PATH, Method.POST, defaultAcceptHeader, response)

            assertThat(report).hasNoErrors()
        }
    }

    companion object : Loggable() {

        const val PATH = "/people"
        val defaultAcceptHeader = ContentType.APPLICATION_JSON
        val validPersonDetails = Person("Bruce".let(::Name), "Wayne".let(::Name), 36)
        const val API_FILE_LOCATION = "api/api.yml"

        fun uri(path: String) = "http://localhost:8080/$path"
    }
}