package org.sollecitom.chassis.ddd.test.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore

internal class InMemoryHistory(private val historical: Flow<Event>, private val queryFactory: InMemoryQueryFactory) : EventStore.History {

    override fun <EVENT : Event> all(query: EventStore.History.Query<EVENT>): Flow<EVENT> = historical.selectedBy(query)

    override suspend fun <EVENT : Event> firstOrNull(query: EventStore.History.Query<EVENT>): EVENT? {

        return all(query).firstOrNull()
    }

    private val <QUERY : EventStore.History.Query<EVENT>, EVENT : Event> QUERY.inMemory: InMemoryHistoryQuery<QUERY, EVENT> get() = queryFactory(query = this) ?: error("Unsupported query type ${type.value}")

    private fun <EVENT : Event> Flow<Event>.selectedBy(query: InMemoryHistoryQuery<*, EVENT>): Flow<EVENT> = filterIsInstance(query.eventType).filter { event -> query.invoke(event) }

    private fun <EVENT : Event> Flow<Event>.selectedBy(query: EventStore.History.Query<EVENT>): Flow<EVENT> = selectedBy(query.inMemory)
}