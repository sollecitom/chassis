package com.element.dpg.libs.chassis.correlation.core.test.utils.access.scope

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.utils.UniqueIdGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.scope.AccessContainer

context(UniqueIdGenerator)
fun AccessContainer.Companion.create(id: Id = newId.internal()): AccessContainer = AccessContainer(id)