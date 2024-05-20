package com.element.dpg.libs.chassis.ddd.domain

import kotlinx.coroutines.Deferred

data class PublishedEvent<out EVENT : Event>(val event: EVENT, val wasPersisted: Deferred<Unit>)