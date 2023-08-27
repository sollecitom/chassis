package org.sollecitom.chassis.correlation.core.domain.access.actor

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant

sealed interface Actor {

    val account: Account
    val benefitingAccount: Account
    val authentication: Authentication

    sealed interface Account {
        val id: Id
        val tenant: Tenant

        companion object
    }

    data class UserAccount(override val id: Id, override val tenant: Tenant) : Account {

        companion object
    }

    data class ServiceAccount(override val id: Id, override val tenant: Tenant) : Account {

        companion object
    }

    companion object
}