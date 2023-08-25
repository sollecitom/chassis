package org.sollecitom.chassis.correlation.core.domain.access.actor

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication

data class ActorOnBehalf<out ID : Id<ID>, out AUTHENTICATION : Authentication>(override val account: Actor.Account<ID>, override val authentication: AUTHENTICATION, override val benefitingAccount: Actor.Account<ID>) : Actor<ID, AUTHENTICATION>

fun <ID : Id<ID>, AUTHENTICATION : Authentication> DirectActor<ID, AUTHENTICATION>.onBehalfOf(benefiting: Actor.Account<ID>): ActorOnBehalf<ID, AUTHENTICATION> = ActorOnBehalf(account = this.account, authentication = this.authentication, benefitingAccount = benefiting)