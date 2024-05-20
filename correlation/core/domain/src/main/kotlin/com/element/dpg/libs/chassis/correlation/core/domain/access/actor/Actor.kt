package com.element.dpg.libs.chassis.correlation.core.domain.access.actor

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.domain.access.customer.Customer
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import java.util.*

sealed interface Actor {

    val account: Account
    val benefitingAccount: Account
    val authentication: Authentication

    sealed interface Account {
        val id: Id
        val customer: Customer
        val tenant: Tenant

        companion object
    }

    data class UserAccount(override val id: Id, val locale: Locale, override val customer: Customer, override val tenant: Tenant) : Account {

        companion object
    }

    data class ServiceAccount(override val id: Id, override val customer: Customer, override val tenant: Tenant) : Account {

        companion object
    }

    companion object
}

val Actor.Account.localeOrNull: Locale? get() = if (this is Actor.UserAccount) locale else null
val Actor.Account.localeOrDefault: Locale get() = localeOrNull ?: Locale.getDefault()

val Actor.tenant: Tenant get() = account.tenant
val Actor.customer: Customer get() = account.customer