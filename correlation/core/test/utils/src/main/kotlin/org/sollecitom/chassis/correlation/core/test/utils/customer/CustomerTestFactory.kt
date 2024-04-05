package org.sollecitom.chassis.correlation.core.test.utils.customer

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.correlation.core.domain.access.customer.Customer
import org.sollecitom.chassis.correlation.core.domain.tenancy.Tenant

context(UniqueIdGenerator)
fun Customer.Companion.create(id: Id = newId.internal()): Customer = Customer(id)