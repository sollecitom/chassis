package org.sollecitom.chassis.openapi.validation.http4k.validator

import assertk.assertThat
import assertk.assertions.isSuccess
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.http4k.utils.lens.body
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.openapi.parser.OpenApiReader
import org.sollecitom.chassis.test.utils.assertions.failedThrowing

// TODO refactor this whole mess
@TestInstance(PER_CLASS)
private class Http4kRequestOpenApiValidationTests {

    private val openApi = OpenApiReader.parse(API_FILE_LOCATION)
    private val validator = StandardHttp4KOpenApiValidator(openApi = openApi, jsonSchemasDirectoryName = "", rejectUnknownRequestParameters = true, rejectUnknownResponseHeaders = true)
//    private val validator = StandardHttp4KOpenApiValidator(openApi = openApi, jsonSchemasDirectoryName = "schemas/json", rejectUnknownRequestParameters = true, rejectUnknownResponseHeaders = true)

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

            val result = runCatching { validator.validate(request) }

            assertThat(result).isSuccess()
        }

        @Test
        fun `are rejected as invalid when a required header is missing`() {


            val json = validPersonDetails.toJson()
            val request = validRequest(json).removeHeader(requiredRequestHeaderName)

            val result = runCatching { validator.validate(request) }

            assertThat(result).failedThrowing<Http4kOpenApiValidationException>()
        }

        @Test
        fun `are rejected as invalid when an unknown header is specified`() {

            val json = validPersonDetails.toJson()
            val request = Request(Method.POST, uri(PATH)).body(json).header("Unknown-Header", "unknown header value")

            val result = runCatching { validator.validate(request) }

            assertThat(result).failedThrowing<Http4kOpenApiValidationException>()
        }

        @Test
        fun `are rejected as invalid when the body is invalid`() {


            val invalidJson = validPersonDetails.toJson().apply { remove("firstName") }
            val request = Request(Method.POST, uri(PATH)).body(invalidJson)

            val result = runCatching { validator.validate(request) }

            assertThat(result).failedThrowing<Http4kOpenApiValidationException>()
        }

        @Test
        fun `are rejected as invalid when the body is invalid JSON type`() {


            val json = sequenceOf(validPersonDetails.toJson(), validPersonDetails.toJson()).fold(JSONArray(), JSONArray::put)
            val request = validRequest(json)

            val result = runCatching { validator.validate(request) }

            assertThat(result).failedThrowing<Http4kOpenApiValidationException>()
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

            val result = runCatching { validator.validate(PATH, Method.POST, DEFAULT_ACCEPT_HEADER, response) }

            assertThat(result).isSuccess()
        }

        @Test
        fun `are rejected as invalid when a required header is missing`() {


            val json = validPersonDetails.toJson()
            val response = validResponse(json).removeHeader(requiredResponseHeaderName)

            val result = runCatching { validator.validate(PATH, Method.POST, DEFAULT_ACCEPT_HEADER, response) }

            assertThat(result).failedThrowing<Http4kOpenApiValidationException>()
        }

        @Test
        fun `are rejected as invalid when an unknown header is specified`() {


            val json = validPersonDetails.toJson()
            val response = validResponse(json).header("Unknown-Response-Header", "value")

            val result = runCatching { validator.validate(PATH, Method.POST, DEFAULT_ACCEPT_HEADER, response) }

            assertThat(result).failedThrowing<Http4kOpenApiValidationException>()
        }

        @Test
        fun `are rejected as invalid when the body is invalid`() {

            val invalidJson = validPersonDetails.toJson().apply { remove("firstName") }
            val response = validResponse(invalidJson)

            val result = runCatching { validator.validate(PATH, Method.POST, DEFAULT_ACCEPT_HEADER, response) }

            assertThat(result).failedThrowing<Http4kOpenApiValidationException>()
        }

        @Test
        fun `are rejected as invalid when the body is invalid JSON type`() {


            val json = sequenceOf(validPersonDetails.toJson(), validPersonDetails.toJson()).fold(JSONArray(), JSONArray::put)
            val response = validResponse(json)

            val result = runCatching { validator.validate(PATH, Method.POST, DEFAULT_ACCEPT_HEADER, response) }

            assertThat(result).failedThrowing<Http4kOpenApiValidationException>()
        }

        @Test
        fun `are confirmed valid when valid with nested objects`() {

            val json = JSONObject("""{"errors":[{"message":"First name cannot be equal to last name"}]}""")
            val response = validResponse(json = json, status = Status.UNPROCESSABLE_ENTITY)

            val result = runCatching { validator.validate(PATH, Method.POST, DEFAULT_ACCEPT_HEADER, response) }

            assertThat(result).isSuccess()
        }
    }

    companion object : Loggable() {

        const val PATH = "/people"
        const val DEFAULT_ACCEPT_HEADER = "application/json"
        val validPersonDetails = Person("Bruce".let(::Name), "Wayne".let(::Name), 36)
        const val API_FILE_LOCATION = "api/api.yml"

        fun uri(path: String) = "http://localhost:8080/$path"
    }
}