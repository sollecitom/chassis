package org.sollecitom.chassis.ddd.store.memory

import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore

internal object NoSupportedQueriesInMemoryQueryFactory : InMemoryEventStoreQueryFactory {

    override fun <EVENT : Event> invoke(query: EventStore.Query<EVENT>): InMemoryEventStoreQuery<EVENT>? {
        return null
    }
}