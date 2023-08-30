package org.sollecitom.chassis.core.domain.identity

@JvmInline
value class StringId(override val stringValue: String) : Id {

    init {
        require(stringValue.isNotBlank()) { "ID value cannot be blank" }
    }
}