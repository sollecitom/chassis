package org.sollecitom.chassis.correlation.core.test.utils.access.actor

import org.sollecitom.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.actor.DirectActor
import org.sollecitom.chassis.correlation.core.domain.access.authentication.Authentication
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.test.utils.access.authentication.credentialsBased
import org.sollecitom.chassis.correlation.core.test.utils.tenancy.create

context(WithCoreGenerators)
fun Actor.Account.Companion.user(id: SortableTimestampedUniqueIdentifier<*> = newId.ulid(), tenant: Tenant = Tenant.create()): Actor.UserAccount<SortableTimestampedUniqueIdentifier<*>> = Actor.UserAccount(id, tenant)

context(WithCoreGenerators)
fun Actor.UserAccount.Companion.create(id: SortableTimestampedUniqueIdentifier<*> = newId.ulid(), tenant: Tenant = Tenant.create()): Actor.UserAccount<SortableTimestampedUniqueIdentifier<*>> = Actor.UserAccount(id, tenant)

context(WithCoreGenerators)
fun Actor.Account.Companion.service(id: SortableTimestampedUniqueIdentifier<*> = newId.ulid(), tenant: Tenant = Tenant.create()): Actor.ServiceAccount<SortableTimestampedUniqueIdentifier<*>> = Actor.ServiceAccount(id, tenant)

context(WithCoreGenerators)
fun Actor.ServiceAccount.Companion.create(id: SortableTimestampedUniqueIdentifier<*> = newId.ulid(), tenant: Tenant = Tenant.create()): Actor.ServiceAccount<SortableTimestampedUniqueIdentifier<*>> = Actor.ServiceAccount(id, tenant)

context(WithCoreGenerators)
fun Actor.Companion.direct(account: Actor.Account<SortableTimestampedUniqueIdentifier<*>> = Actor.UserAccount.create(), authentication: Authentication = Authentication.credentialsBased()): DirectActor<SortableTimestampedUniqueIdentifier<*>> = DirectActor(account, authentication)

context(WithCoreGenerators)
fun DirectActor.Companion.create(account: Actor.Account<SortableTimestampedUniqueIdentifier<*>> = Actor.UserAccount.create(), authentication: Authentication = Authentication.credentialsBased()): DirectActor<SortableTimestampedUniqueIdentifier<*>> = DirectActor(account, authentication)