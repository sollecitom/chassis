package org.sollecitom.chassis.ddd.test.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore

internal class InMemoryHistory(private val historical: Flow<Event>, private val queryFactory: InMemoryQueryFactory) : EventStore.History {

    override fun all(): Flow<Event> = historical

    override suspend fun <EVENT : Event> firstOrNull(query: EventStore.History.Query<EVENT>): EVENT? {

        val inMemoryQuery = queryFactory(query) ?: error("Unsupported query type ${query.type.value}")
        return all().selectedBy(inMemoryQuery).firstOrNull()
    }

    private fun <EVENT : Event> Flow<Event>.selectedBy(query: InMemoryHistoryQuery<*, EVENT>): Flow<EVENT> = filterIsInstance(query.eventType).filter { event -> query.invoke(event) }
}

