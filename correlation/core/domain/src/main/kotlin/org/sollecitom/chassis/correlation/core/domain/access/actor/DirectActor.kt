package org.sollecitom.chassis.correlation.core.domain.access.actor

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication

data class DirectActor<out ID : Id<ID>, out AUTHENTICATION : Authentication>(override val account: Actor.Account<ID>, override val authentication: AUTHENTICATION) : Actor<ID, AUTHENTICATION> {

    override val benefitingAccount: Actor.Account<ID>
        get() = account

    companion object
}