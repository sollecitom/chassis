package org.sollecitom.chassis.ddd.events.test.specification

import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore
import org.sollecitom.chassis.ddd.domain.EventStream
import org.sollecitom.chassis.ddd.domain.Events
import org.sollecitom.chassis.ddd.event.store.test.specification.EventStoreTestSpecification
import org.sollecitom.chassis.ddd.event.stream.test.specification.EventStreamTestSpecification
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface EventsTestSpecification : EventStreamTestSpecification, EventStoreTestSpecification {

    override val timeout: Duration get() = 10.seconds
    fun events(): Events.Mutable

    override fun historicalEvents() = events()

    override fun EventStore.Mutable<Event>.forEntityId(entityId: Id) = (this as Events.Mutable).forEntityId(entityId)

    override fun eventStream() = events()

    override fun EventStream.Mutable<Event>.forEntityId(entityId: Id) = (this as Events.Mutable).forEntityId(entityId)
}