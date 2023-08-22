package org.sollecitom.chassis.openapi.validation.http4k.validator

// TODO do we need this wrapping? Maybe a thin extension on OpenApiInteractionValidator instead?
class Http4kOpenApiValidationException(errors: List<String>) : RuntimeException(errors.joinToString(System.lineSeparator()))