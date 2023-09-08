package org.sollecitom.chassis.ddd.event.store.memory

import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore

interface InMemoryEventStoreQueryFactory {

    operator fun <EVENT : Event> invoke(query: EventStore.Query<EVENT>): InMemoryEventStoreQuery<EVENT>?
}