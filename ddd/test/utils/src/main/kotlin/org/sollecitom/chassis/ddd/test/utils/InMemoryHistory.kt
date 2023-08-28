package org.sollecitom.chassis.ddd.test.utils

import kotlinx.coroutines.flow.Flow
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore

@JvmInline
internal value class InMemoryHistory(private val historical: Flow<Event>) : EventStore.History {

    override fun all(): Flow<Event> = historical
}