package org.sollecitom.chassis.correlation.core.test.utils.access.idp

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.correlation.core.domain.access.customer.Customer
import org.sollecitom.chassis.correlation.core.domain.access.idp.IdentityProvider
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant
import org.sollecitom.chassis.correlation.core.test.utils.customer.create
import org.sollecitom.chassis.correlation.core.test.utils.tenancy.create

context(UniqueIdGenerator)
fun IdentityProvider.Companion.create(name: Name = "Octa".let(::Name), customer: Customer = Customer.create(), tenant: Tenant = Tenant.create()): IdentityProvider = IdentityProvider(name, customer, tenant)