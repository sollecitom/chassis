package org.sollecitom.chassis.correlation.core.domain.access.actor

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication

data class ImpersonatingActor<out ID : Id<ID>>(val impersonator: Actor.Account<ID>, val impersonated: Actor.Account<ID>, override val authentication: Authentication) : Actor<ID> {

    override val account: Actor.Account<ID>
        get() = impersonated

    override val benefitingAccount: Actor.Account<ID>
        get() = impersonated
}

fun <ID : Id<ID>> DirectActor<ID>.impersonating(impersonated: Actor.Account<ID>): ImpersonatingActor<ID> = ImpersonatingActor(impersonator = this.account, authentication = this.authentication, impersonated = impersonated)