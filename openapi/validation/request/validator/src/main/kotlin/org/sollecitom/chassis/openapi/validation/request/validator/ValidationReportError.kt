package org.sollecitom.chassis.openapi.validation.request.validator

sealed class ValidationReportError(val key: String) {

    sealed class Request(key: String) : ValidationReportError(key) {

        data object MissingRequiredHeader : Request("validation.request.parameter.header.missing")

        data object UnknownHeader : Request("validation.request.parameter.header.unknown")

        sealed class Body(key: String) : Request(key) {

            data object MissingRequiredField : Body("validation.request.body.schema.required")

            data object InvalidType : Body("validation.request.body.schema.type")
        }
    }

    sealed class Response(key: String) : ValidationReportError(key) {

        data object UnknownHeader : Response("validation.response.header.unknown")
    }
}