package com.element.dpg.libs.chassis.ddd.domain

import com.element.dpg.libs.chassis.core.domain.identity.Id
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance

fun Flow<Event>.filterIsForEntityId(entityId: Id): Flow<EntityEvent> = filterIsInstance<EntityEvent>().filter { it.entityId == entityId }