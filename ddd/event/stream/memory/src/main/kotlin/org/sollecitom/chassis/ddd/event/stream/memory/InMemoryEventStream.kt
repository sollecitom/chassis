package org.sollecitom.chassis.ddd.event.stream.memory

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.stream.EventStream

class InMemoryEventStream : EventStream.Mutable {

    private val _events = MutableSharedFlow<Event>()

    override suspend fun publish(event: Event) = _events.emit(event)

    override val asFlow: Flow<Event> get() = _events
}