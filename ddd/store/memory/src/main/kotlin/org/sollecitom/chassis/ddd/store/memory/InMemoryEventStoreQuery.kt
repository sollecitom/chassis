package org.sollecitom.chassis.ddd.store.memory

import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore
import kotlin.reflect.KClass

interface InMemoryEventStoreQuery<EVENT : Event> : EventStore.Query<EVENT> {

    val eventType: KClass<EVENT>

    operator fun invoke(event: EVENT): Boolean
}