package com.element.dpg.libs.chassis.ddd.domain

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.domain.naming.Name

interface EntityEvent : Event {

    val entityId: Id
    val entityType: Name

    companion object
}