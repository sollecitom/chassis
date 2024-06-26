package com.element.dpg.libs.chassis.core.domain.email

import com.element.dpg.libs.chassis.kotlin.extensions.text.withoutWhitespace

@JvmInline
value class EmailAddress(val value: String) : Comparable<EmailAddress> {

    init {
        // TODO improve email validation
        require(value.isNotBlank()) { "email address cannot be blank" }
        require(value.withoutWhitespace() == value) { "email address cannot contain whitespace" }
        require(PREFIX_FROM_DOMAIN_SEPARATOR in value) { "email address must contain '$PREFIX_FROM_DOMAIN_SEPARATOR'" }
    }

    override fun compareTo(other: EmailAddress) = value.compareTo(other.value)

    companion object {

        const val PREFIX_FROM_DOMAIN_SEPARATOR = "@"
    }
}