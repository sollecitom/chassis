package org.sollecitom.chassis.core.domain.identity

import org.sollecitom.chassis.core.domain.identity.ulid.ULID

@JvmInline
value class StringId(override val stringValue: String) : Id<StringId> {

    init {
        require(stringValue.isNotBlank()) { "ID value cannot be blank" }
    }
}

fun ULID.asStringId(): StringId = StringId(stringValue)