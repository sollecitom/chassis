package com.element.dpg.libs.chassis.correlation.core.test.utils.customer

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.utils.UniqueIdGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.customer.Customer

context(UniqueIdGenerator)
fun Customer.Companion.create(id: Id = newId.internal()): Customer = Customer(id)