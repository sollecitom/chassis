package org.sollecitom.chassis.ddd.domain.framework

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.store.EventStore
import org.sollecitom.chassis.ddd.domain.stream.EventStream

class CompositeEventFramework(private val stream: EventStream.Mutable, private val store: EventStore.Mutable) : EventFramework.Mutable, EventStream.Mutable by stream, EventStore.Mutable by store {

    override fun forEntityId(entityId: Id): EventFramework.EntitySpecific.Mutable = EntitySpecific(entityId, stream, store)

    class EntitySpecific(override val entityId: Id, stream: EventStream.Mutable, store: EventStore.Mutable) : EventFramework.EntitySpecific.Mutable, EventStream.EntitySpecific.Mutable by stream.forEntityId(entityId), EventStore.EntitySpecific.Mutable by store.forEntityId(entityId)
}