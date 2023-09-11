package org.sollecitom.chassis.ddd.events.pulsar

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.EventStore
import org.sollecitom.chassis.ddd.domain.EventStream
import org.sollecitom.chassis.ddd.domain.Events
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

class CompositeEvents(private val stream: EventStream.Mutable, private val store: EventStore.Mutable) : Events.Mutable, EventStream.Mutable by stream, EventStore.Mutable by store {

    override fun forEntityId(entityId: Id): Events.EntitySpecific.Mutable = EntitySpecific(entityId, stream, store)

    class EntitySpecific(override val entityId: Id, stream: EventStream.Mutable, store: EventStore.Mutable) : Events.EntitySpecific.Mutable, EventStream.EntitySpecific.Mutable by stream.forEntityId(entityId), EventStore.EntitySpecific.Mutable by store.forEntityId(entityId)
}