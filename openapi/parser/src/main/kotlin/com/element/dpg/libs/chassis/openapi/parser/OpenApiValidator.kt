package com.element.dpg.libs.chassis.openapi.parser

import java.net.URL

interface OpenApiValidator {

    fun validate(openApiLocation: String): Result

    sealed interface Result {

        val valid: Boolean

        data class Invalid(val errors: Set<Error>) : Result {

            override val valid = false
        }

        object Valid : Result {

            override val valid = true
        }
    }

    data class Error(val message: String)
}

fun OpenApiValidator.validate(swaggerLocation: URL): OpenApiValidator.Result = validate(swaggerLocation.toString())