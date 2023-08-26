package org.sollecitom.chassis.correlation.core.domain.access.actor

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant

sealed interface Actor<out ID : Id<ID>> {

    val account: Account<ID>
    val benefitingAccount: Account<ID>
    val authentication: Authentication

    sealed interface Account<out ID : Id<ID>> {
        val id: ID
        val tenant: Tenant

        companion object
    }

    data class UserAccount<out ID : Id<ID>>(override val id: ID, override val tenant: Tenant) : Account<ID> {

        companion object
    }

    data class ServiceAccount<out ID : Id<ID>>(override val id: ID, override val tenant: Tenant) : Account<ID> {

        companion object
    }

    companion object
}