package org.sollecitom.chassis.example.event.domain.predicate.search

import org.sollecitom.chassis.core.domain.email.EmailAddress
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.ddd.domain.Happening
import org.sollecitom.chassis.example.event.domain.user.registration.RegisterUser

data class FindPredicateDevice(val emailAddress: EmailAddress, val device: Device) : Command<RegisterUser.Result, Access> {

    override val accessRequirements get() = Command.AccessRequirements.UnauthenticatedAccessOnly
    override val type: Happening.Type get() = Companion.type

    override fun toString() = "FindPredicateDevice(emailAddress=${emailAddress.value}, device=${device}, type=${type.name.value}, version=${version.value})"

    sealed interface Result {

        data object Accepted : Result

        sealed interface Rejected : Result {

            // TODO can this ever be rejected e.g., based on a blacklist or whitelist of email domains?
            data class UnsupportedPersonalEmailAddress(val emailAddress: EmailAddress) : Rejected
        }
    }

    companion object {
        val type = Happening.Type("find-predicate-device".let(::Name), 1.let(::IntVersion))
    }
}