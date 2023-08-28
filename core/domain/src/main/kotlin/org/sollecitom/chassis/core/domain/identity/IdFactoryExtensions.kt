package org.sollecitom.chassis.core.domain.identity

import org.sollecitom.chassis.core.domain.identity.ulid.ULID

fun Id.Companion.fromString(stringValue: String): Id {

    return runCatching { ULID(stringValue) }.getOrElse { StringId(stringValue) }
}