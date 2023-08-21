package org.sollecitom.chassis.openapi.checker.model

data class OpenApiField<in TARGET : Any, out VALUE>(val name: String, val getter: (TARGET) -> VALUE)