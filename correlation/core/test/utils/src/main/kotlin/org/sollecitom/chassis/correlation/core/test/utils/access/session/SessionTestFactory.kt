package org.sollecitom.chassis.correlation.core.test.utils.access.session

import org.sollecitom.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.correlation.core.domain.access.customer.Customer
import org.sollecitom.chassis.correlation.core.domain.access.idp.IdentityProvider
import org.sollecitom.chassis.correlation.core.domain.access.session.FederatedSession
import org.sollecitom.chassis.correlation.core.domain.access.session.Session
import org.sollecitom.chassis.correlation.core.domain.access.session.SimpleSession
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.test.utils.access.idp.create
import org.sollecitom.chassis.correlation.core.test.utils.customer.create
import org.sollecitom.chassis.correlation.core.test.utils.tenancy.create

context(UniqueIdGenerator)
fun Session.Companion.simple(id: Id = newId.internal()): SimpleSession = SimpleSession(id)

context(UniqueIdGenerator)
fun Session.Companion.federated(customer: Customer = Customer.create(), tenant: Tenant = Tenant.create(), id: Id = newId.internal(), identityProvider: IdentityProvider = IdentityProvider.create(customer = customer, tenant = tenant)): FederatedSession = FederatedSession(id, identityProvider)

context(UniqueIdGenerator)
fun Session.Companion.federated(identityProvider: IdentityProvider, id: Id = newId.internal()): FederatedSession = FederatedSession(id, identityProvider)