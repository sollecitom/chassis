package org.sollecitom.chassis.example.service.endpoint.write.adapters.driven.pulsar

import kotlinx.coroutines.flow.Flow
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.EntityEventStore
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore

// TODO could this be generic and shared across projects?
// TODO pure pulsar vs pulsar and postgres?
class PulsarEventStore : EventStore.Mutable {

    override suspend fun publish(event: Event) {
        TODO("Not yet implemented")
    }

    override fun forEntity(entityId: Id): EntityEventStore.Mutable {
        TODO("Not yet implemented")
    }

    override val stream: Flow<Event>
        get() = TODO("Not yet implemented")

    override val history: EventStore.History
        get() = TODO("Not yet implemented")
}