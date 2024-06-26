package com.element.dpg.libs.chassis.openapi.checking.checker.rules.utils

internal fun Any.trimmed(): Any? = when (this) {
    is String -> takeIf { it.isNotBlank() }
    else -> this
}