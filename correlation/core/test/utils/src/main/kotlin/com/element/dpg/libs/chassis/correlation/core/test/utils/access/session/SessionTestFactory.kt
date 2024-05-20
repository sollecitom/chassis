package com.element.dpg.libs.chassis.correlation.core.test.utils.access.session

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.utils.UniqueIdGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.customer.Customer
import com.element.dpg.libs.chassis.correlation.core.domain.access.idp.IdentityProvider
import com.element.dpg.libs.chassis.correlation.core.domain.access.session.FederatedSession
import com.element.dpg.libs.chassis.correlation.core.domain.access.session.Session
import com.element.dpg.libs.chassis.correlation.core.domain.access.session.SimpleSession
import com.element.dpg.libs.chassis.correlation.core.domain.tenancy.Tenant
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.idp.create
import com.element.dpg.libs.chassis.correlation.core.test.utils.customer.create
import com.element.dpg.libs.chassis.correlation.core.test.utils.tenancy.create

context(UniqueIdGenerator)
fun Session.Companion.simple(id: Id = newId.internal()): SimpleSession = SimpleSession(id)

context(UniqueIdGenerator)
fun Session.Companion.federated(customer: Customer = Customer.create(), tenant: Tenant = Tenant.create(), id: Id = newId.internal(), identityProvider: IdentityProvider = IdentityProvider.create(customer = customer, tenant = tenant)): FederatedSession = FederatedSession(id, identityProvider)

context(UniqueIdGenerator)
fun Session.Companion.federated(identityProvider: IdentityProvider, id: Id = newId.internal()): FederatedSession = FederatedSession(id, identityProvider)