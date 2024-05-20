package com.element.dpg.libs.chassis.core.domain.identity

fun Id.Companion.fromString(stringValue: String): Id {

    return runCatching { ULID(stringValue) }.getOrElse { StringId(stringValue) }
}