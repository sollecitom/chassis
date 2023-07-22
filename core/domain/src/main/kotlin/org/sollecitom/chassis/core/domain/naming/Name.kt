package org.sollecitom.chassis.core.domain.naming

import java.util.*

@JvmInline
value class Name(val value: String) : Comparable<Name> {

    init {
        require(value.isNotBlank()) { "value cannot be blank" }
    }

    fun uppercase(locale: Locale = Locale.ROOT) = Name(value.uppercase(locale))

    fun lowercase(locale: Locale = Locale.ROOT) = Name(value.lowercase(locale))

    fun capitalized(locale: Locale = Locale.getDefault()) = Name(value.replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() })

    override fun compareTo(other: Name) = value.compareTo(other.value)

    companion object
}