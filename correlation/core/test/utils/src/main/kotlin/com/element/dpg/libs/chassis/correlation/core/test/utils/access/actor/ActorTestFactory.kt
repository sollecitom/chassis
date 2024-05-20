package com.element.dpg.libs.chassis.correlation.core.test.utils.access.actor

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.utils.TimeGenerator
import com.element.dpg.libs.chassis.core.utils.UniqueIdGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.actor.Actor
import com.element.dpg.libs.chassis.correlation.core.domain.access.actor.DirectActor
import com.element.dpg.libs.chassis.correlation.core.domain.access.authentication.Authentication
import com.element.dpg.libs.chassis.correlation.core.domain.access.customer.Customer
import com.element.dpg.libs.chassis.correlation.core.domain.tenancy.Tenant
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.authentication.credentialsBased
import com.element.dpg.libs.chassis.correlation.core.test.utils.customer.create
import com.element.dpg.libs.chassis.correlation.core.test.utils.tenancy.create
import java.util.*

context(UniqueIdGenerator)
fun Actor.Account.Companion.user(id: Id = newId.internal(), locale: Locale = Locale.UK, customer: Customer = Customer.create(), tenant: Tenant = Tenant.create()): Actor.UserAccount = Actor.UserAccount(id, locale, customer, tenant)

context(UniqueIdGenerator)
fun Actor.UserAccount.Companion.create(id: Id = newId.internal(), locale: Locale = Locale.UK, customer: Customer = Customer.create(), tenant: Tenant = Tenant.create()): Actor.UserAccount = Actor.UserAccount(id, locale, customer, tenant)

context(UniqueIdGenerator)
fun Actor.Account.Companion.service(id: Id = newId.internal(), customer: Customer = Customer.create(), tenant: Tenant = Tenant.create()): Actor.ServiceAccount = Actor.ServiceAccount(id, customer, tenant)

context(UniqueIdGenerator)
fun Actor.ServiceAccount.Companion.create(id: Id = newId.internal(), customer: Customer = Customer.create(), tenant: Tenant = Tenant.create()): Actor.ServiceAccount = Actor.ServiceAccount(id, customer, tenant)

context(UniqueIdGenerator, TimeGenerator)
fun Actor.Companion.direct(account: Actor.Account = Actor.UserAccount.create(), authentication: Authentication = Authentication.credentialsBased()): DirectActor = DirectActor(account, authentication)

context(UniqueIdGenerator, TimeGenerator)
fun DirectActor.Companion.create(account: Actor.Account = Actor.UserAccount.create(), authentication: Authentication = Authentication.credentialsBased()): DirectActor = DirectActor(account, authentication)