package com.element.dpg.libs.chassis.ddd.domain

import kotlinx.coroutines.Deferred

data class PublishedEvent<out EVENT : com.element.dpg.libs.chassis.ddd.domain.Event>(val event: EVENT, val wasPersisted: Deferred<Unit>)