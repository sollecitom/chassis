package org.sollecitom.chassis.ddd.store.memory

import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore
import kotlin.reflect.KClass

interface InMemoryEventStoreQuery<QUERY : EventStore.Query<EVENT>, EVENT : Event> {

    val eventType: KClass<EVENT>

    operator fun invoke(event: EVENT): Boolean
}