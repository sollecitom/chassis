package com.element.dpg.libs.chassis.correlation.core.test.utils.tenancy

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.utils.UniqueIdGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.tenancy.Tenant

context(UniqueIdGenerator)
fun Tenant.Companion.create(id: Id = newId.internal()): Tenant = Tenant(id)