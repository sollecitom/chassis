package com.element.dpg.libs.chassis.openapi.checking.checker.model

data class OpenApiField<in TARGET : Any, out VALUE>(val name: String, val getter: (TARGET) -> VALUE) {

    override fun toString() = "OpenApiField('$name')"
}