package com.element.dpg.libs.chassis.correlation.core.domain.access.actor

import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication

data class ActorOnBehalf(override val account: Actor.Account, override val authentication: Authentication, override val benefitingAccount: Actor.Account) : Actor {

    companion object
}

fun DirectActor.onBehalfOf(benefiting: Actor.Account): ActorOnBehalf = ActorOnBehalf(account = this.account, authentication = this.authentication, benefitingAccount = benefiting)