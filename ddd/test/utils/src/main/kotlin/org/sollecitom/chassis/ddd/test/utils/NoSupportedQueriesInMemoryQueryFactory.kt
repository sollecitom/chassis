package org.sollecitom.chassis.ddd.test.utils

import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore

internal object NoSupportedQueriesInMemoryQueryFactory : InMemoryQueryFactory {

    override fun <IN_MEMORY_QUERY : InMemoryHistoryQuery<QUERY, EVENT>, QUERY : EventStore.History.Query<EVENT>, EVENT : Event> invoke(query: QUERY): IN_MEMORY_QUERY? {
        return null
    }
}