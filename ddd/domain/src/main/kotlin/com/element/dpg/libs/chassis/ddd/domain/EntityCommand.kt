package com.element.dpg.libs.chassis.ddd.domain

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.correlation.core.domain.access.Access

interface EntityCommand<out RESULT, out ACCESS : Access> : Command<RESULT, ACCESS> {

    val entityId: Id
    val entityType: Name

    companion object
}