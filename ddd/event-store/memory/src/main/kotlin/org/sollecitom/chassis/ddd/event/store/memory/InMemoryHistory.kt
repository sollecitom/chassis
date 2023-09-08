package org.sollecitom.chassis.ddd.event.store.memory

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore

internal class InMemoryHistory(private val historical: Flow<Event>, private val queryFactory: InMemoryEventStoreQueryFactory) : EventStore.History {

    override fun <EVENT : Event> all(query: EventStore.Query<EVENT>): Flow<EVENT> = historical.selectedBy(query)

    override suspend fun <EVENT : Event> firstOrNull(query: EventStore.Query<EVENT>): EVENT? {

        return all(query).firstOrNull()
    }

    private val <QUERY : EventStore.Query<EVENT>, EVENT : Event> QUERY.inMemory: InMemoryEventStoreQuery<EVENT> get() = queryFactory(query = this) ?: error("Unsupported query $this")

    private fun <EVENT : Event> Flow<Event>.selectedBy(query: InMemoryEventStoreQuery<EVENT>): Flow<EVENT> = filterIsInstance(query.eventType).filter { event -> query.invoke(event) }

    private fun <EVENT : Event> Flow<Event>.selectedBy(query: EventStore.Query<EVENT>): Flow<EVENT> = selectedBy(query.inMemory)
}