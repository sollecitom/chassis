package com.element.dpg.libs.chassis.correlation.core.test.utils.access.idp

import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.utils.UniqueIdGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.customer.Customer
import com.element.dpg.libs.chassis.correlation.core.domain.access.idp.IdentityProvider
import com.element.dpg.libs.chassis.correlation.core.domain.tenancy.Tenant
import com.element.dpg.libs.chassis.correlation.core.test.utils.customer.create
import com.element.dpg.libs.chassis.correlation.core.test.utils.tenancy.create

context(UniqueIdGenerator)
fun IdentityProvider.Companion.create(name: Name = "Octa".let(::Name), customer: Customer = Customer.create(), tenant: Tenant = Tenant.create()): IdentityProvider = IdentityProvider(name, customer, tenant)