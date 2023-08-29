package org.sollecitom.chassis.correlation.core.domain.access.actor

import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication

data class ImpersonatingActor(val impersonator: Actor.Account, val impersonated: Actor.Account, override val authentication: Authentication) : Actor {

    override val account: Actor.Account
        get() = impersonated

    override val benefitingAccount: Actor.Account
        get() = impersonated

    companion object
}

fun DirectActor.impersonating(impersonated: Actor.Account): ImpersonatingActor = ImpersonatingActor(impersonator = this.account, authentication = this.authentication, impersonated = impersonated)