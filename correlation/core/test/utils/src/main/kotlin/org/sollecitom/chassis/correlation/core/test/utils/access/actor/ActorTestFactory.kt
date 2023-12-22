package org.sollecitom.chassis.correlation.core.test.utils.access.actor

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.TimeGenerator
import org.sollecitom.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.actor.DirectActor
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.test.utils.access.authentication.credentialsBased
import org.sollecitom.chassis.correlation.core.test.utils.tenancy.create
import java.util.*

context(UniqueIdGenerator)
fun Actor.Account.Companion.user(id: Id = newId.internal(), locale: Locale = Locale.UK, tenant: Tenant = Tenant.create()): Actor.UserAccount = Actor.UserAccount(id, locale, tenant)

context(UniqueIdGenerator)
fun Actor.UserAccount.Companion.create(id: Id = newId.internal(), locale: Locale = Locale.UK, tenant: Tenant = Tenant.create()): Actor.UserAccount = Actor.UserAccount(id, locale, tenant)

context(UniqueIdGenerator)
fun Actor.Account.Companion.service(id: Id = newId.internal(), tenant: Tenant = Tenant.create()): Actor.ServiceAccount = Actor.ServiceAccount(id, tenant)

context(UniqueIdGenerator)
fun Actor.ServiceAccount.Companion.create(id: Id = newId.internal(), tenant: Tenant = Tenant.create()): Actor.ServiceAccount = Actor.ServiceAccount(id, tenant)

context(UniqueIdGenerator, TimeGenerator)
fun Actor.Companion.direct(account: Actor.Account = Actor.UserAccount.create(), authentication: Authentication = Authentication.credentialsBased()): DirectActor = DirectActor(account, authentication)

context(UniqueIdGenerator, TimeGenerator)
fun DirectActor.Companion.create(account: Actor.Account = Actor.UserAccount.create(), authentication: Authentication = Authentication.credentialsBased()): DirectActor = DirectActor(account, authentication)