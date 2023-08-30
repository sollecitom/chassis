package org.sollecitom.chassis.core.domain.identity

fun Id.Companion.fromString(stringValue: String): Id {

    return runCatching { ULID(stringValue) }.getOrElse { StringId(stringValue) }
}