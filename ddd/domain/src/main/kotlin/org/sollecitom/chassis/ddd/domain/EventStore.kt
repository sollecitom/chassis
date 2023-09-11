package org.sollecitom.chassis.ddd.domain

import kotlinx.coroutines.flow.Flow

interface EventStore<in EVENT : Event> {

    fun <E : EVENT> all(query: Query<E> = Query.Unrestricted): Flow<E>

    suspend fun <E : EVENT> firstOrNull(query: Query<E>): E?

    interface Mutable<in EVENT : Event> : EventStore<EVENT> {

        suspend fun store(event: EVENT)
    }

    interface Query<in EVENT : Event> {

        data object Unrestricted : Query<Event> // TODO add other default queries e.g. timestamps, by ID, etc.
    }

    companion object
}