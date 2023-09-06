package org.sollecitom.chassis.ddd.domain

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name

interface EntityEvent : Event {

    val entityId: Id
    val entityType: Name

    override val type: Type // TODO do we need this?

    companion object

    interface Type : Event.Type {

        companion object
    }
}