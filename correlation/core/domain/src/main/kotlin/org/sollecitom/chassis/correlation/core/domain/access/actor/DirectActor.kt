package org.sollecitom.chassis.correlation.core.domain.access.actor

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication

data class DirectActor<out ID : Id<ID>>(override val account: Actor.Account<ID>, override val authentication: Authentication) : Actor<ID> {

    override val benefitingAccount: Actor.Account<ID>
        get() = account

    companion object
}