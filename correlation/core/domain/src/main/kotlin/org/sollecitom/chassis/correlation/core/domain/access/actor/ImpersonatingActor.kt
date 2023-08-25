package org.sollecitom.chassis.correlation.core.domain.access.actor

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication

data class ImpersonatingActor<out ID : Id<ID>, out AUTHENTICATION : Authentication>(val impersonator: Actor.Account<ID>, val impersonated: Actor.Account<ID>, override val authentication: AUTHENTICATION) : Actor<ID, AUTHENTICATION> {

    override val account: Actor.Account<ID>
        get() = impersonated

    override val benefitingAccount: Actor.Account<ID>
        get() = impersonated
}

fun <ID : Id<ID>, AUTHENTICATION : Authentication> DirectActor<ID, AUTHENTICATION>.impersonating(impersonated: Actor.Account<ID>): ImpersonatingActor<ID, AUTHENTICATION> = ImpersonatingActor(impersonator = this.account, authentication = this.authentication, impersonated = impersonated)