package org.sollecitom.chassis.example.command_endpoint.domain.predicate.search

import org.sollecitom.chassis.core.domain.email.EmailAddress

private class DomainBlacklistEmailAddressValidator(private val domainBlacklist: Set<String>) : EmailAddressValidator {

    override fun validate(emailAddress: EmailAddress): EmailAddressValidator.Result {

        if (emailAddress.domain in domainBlacklist) return EmailAddressValidator.Result.Failure.BlacklistedEmailDomain(emailAddress.domain)
        return EmailAddressValidator.Result.Success
    }

    private val EmailAddress.domain: String get() = value.substringAfter(EmailAddress.PREFIX_FROM_DOMAIN_SEPARATOR)
}

val EmailAddressValidator.Companion.NoOp: EmailAddressValidator get() = withDomainBlacklist(blacklist = emptySet())

fun EmailAddressValidator.Companion.withDomainBlacklist(blacklist: Set<String>): EmailAddressValidator = DomainBlacklistEmailAddressValidator(domainBlacklist = blacklist)