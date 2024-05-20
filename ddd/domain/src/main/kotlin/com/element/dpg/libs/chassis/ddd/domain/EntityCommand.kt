package com.element.dpg.libs.chassis.ddd.domain

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.correlation.core.domain.access.Access

interface EntityCommand<out RESULT, out ACCESS : _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access> : Command<RESULT, ACCESS> {

    val entityId: Id
    val entityType: Name

    companion object
}