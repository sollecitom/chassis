package org.sollecitom.chassis.ddd.event.store.memory

import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore
import kotlin.reflect.KClass

interface InMemoryEventStoreQuery<EVENT : Event> : EventStore.Query<EVENT> {

    val eventType: KClass<EVENT>

    operator fun invoke(event: EVENT): Boolean

    data object Unrestricted : InMemoryEventStoreQuery<Event> {

        override val eventType: KClass<Event> get() = Event::class

        override fun invoke(event: Event) = true
    }
}