package com.element.dpg.libs.chassis.ddd.domain

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name

interface EntityEvent : Event {

    val entityId: Id
    val entityType: Name

    companion object
}