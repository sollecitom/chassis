package com.element.dpg.libs.chassis.ddd.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import org.sollecitom.chassis.core.domain.identity.Id

fun Flow<Event>.filterIsForEntityId(entityId: Id): Flow<EntityEvent> = filterIsInstance<EntityEvent>().filter { it.entityId == entityId }