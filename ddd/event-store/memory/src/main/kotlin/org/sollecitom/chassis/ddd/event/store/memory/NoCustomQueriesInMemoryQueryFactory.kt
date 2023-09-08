package org.sollecitom.chassis.ddd.event.store.memory

import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore

internal object NoCustomQueriesInMemoryQueryFactory : InMemoryEventStoreQueryFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <EVENT : Event> invoke(query: EventStore.Query<EVENT>) = when (query) {
        is EventStore.Query.Unrestricted -> InMemoryEventStoreQuery.Unrestricted as InMemoryEventStoreQuery<EVENT>
        else -> null
    }
}