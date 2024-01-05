package org.sollecitom.chassis.ddd.domain

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.correlation.core.domain.access.Access

interface EntityCommand<out RESULT, out ACCESS : Access> : Command<RESULT, ACCESS> {

    val entityId: Id
    val entityType: Name

    companion object
}