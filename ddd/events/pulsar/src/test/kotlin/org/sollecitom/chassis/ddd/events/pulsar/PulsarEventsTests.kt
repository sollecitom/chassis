package org.sollecitom.chassis.ddd.events.pulsar

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.*
import org.sollecitom.chassis.ddd.events.test.specification.EventsTestSpecification

@TestInstance(PER_CLASS)
@Disabled
private class PulsarEventsTests : EventsTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    override fun events() = pulsarStreamWithMaterializedViewInPostgres()
}

fun pulsarStreamWithMaterializedViewInMemory(): CompositeEvents {

    TODO("implement")
}

fun pulsarStreamWithMaterializedViewInPostgres(): CompositeEvents {

    TODO("implement")
}

class CompositeEvents(private val stream: EventStream.Mutable<Event>, private val entitySpecificStream: EventStream.Mutable<Event>.(entityId: Id) -> EventStream.Mutable<EntityEvent>, private val store: EventStore.Mutable<Event>, private val entitySpecificStore: EventStore.Mutable<Event>.(entityId: Id) -> EventStore.Mutable<EntityEvent>) : Events.Mutable, EventStream.Mutable<Event> by stream, EventStore.Mutable<Event> by store {

    override fun forEntityId(entityId: Id): EntitySpecificEvents.Mutable = EntitySpecific(entityId, stream.entitySpecificStream(entityId), store.entitySpecificStore(entityId))

    class EntitySpecific(override val entityId: Id, stream: EventStream.Mutable<EntityEvent>, store: EventStore.Mutable<EntityEvent>) : EntitySpecificEvents.Mutable, EventStream.Mutable<EntityEvent> by stream, EventStore.Mutable<EntityEvent> by store
}