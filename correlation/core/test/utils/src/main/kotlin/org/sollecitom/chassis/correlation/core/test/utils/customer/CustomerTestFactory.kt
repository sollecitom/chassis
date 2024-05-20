package org.sollecitom.chassis.correlation.core.test.utils.customer

import org.sollecitom.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.correlation.core.domain.access.customer.Customer

context(UniqueIdGenerator)
fun Customer.Companion.create(id: Id = newId.internal()): Customer = Customer(id)