package org.sollecitom.chassis.correlation.core.domain.access.actor

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication

data class ActorOnBehalf<out ID : Id<ID>>(override val account: Actor.Account<ID>, override val authentication: Authentication, override val benefitingAccount: Actor.Account<ID>) : Actor<ID>

fun <ID : Id<ID>> DirectActor<ID>.onBehalfOf(benefiting: Actor.Account<ID>): ActorOnBehalf<ID> = ActorOnBehalf(account = this.account, authentication = this.authentication, benefitingAccount = benefiting)