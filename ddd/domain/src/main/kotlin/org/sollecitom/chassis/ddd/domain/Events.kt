package org.sollecitom.chassis.ddd.domain

import org.sollecitom.chassis.core.domain.identity.Id

// TODO rename to EventFramework
interface Events : EventStream, EventStore {

    override fun forEntityId(entityId: Id): EntitySpecific

    interface Mutable : Events, EventStream.Mutable, EventStore.Mutable {

        override fun forEntityId(entityId: Id): EntitySpecific.Mutable
    }

    interface EntitySpecific : EventStream.EntitySpecific, EventStore.EntitySpecific {

        override val entityId: Id

        interface Mutable : EntitySpecific, EventStream.EntitySpecific.Mutable, EventStore.EntitySpecific.Mutable
    }

    companion object
}