package org.sollecitom.chassis.ddd.domain.framework

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.ddd.domain.stream.EventStream

interface EventFramework : EventStream, EventStore {

    override fun forEntityId(entityId: Id): EntitySpecific

    interface Mutable : EventFramework, EventStream.Mutable, EventStore.Mutable {

        override fun forEntityId(entityId: Id): EntitySpecific.Mutable

        companion object
    }

    interface EntitySpecific : EventStream.EntitySpecific, EventStore.EntitySpecific {

        override val entityId: Id

        interface Mutable : EntitySpecific, EventStream.EntitySpecific.Mutable, EventStore.EntitySpecific.Mutable
    }

    companion object
}