package org.sollecitom.chassis.ddd.event.store.memory

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.EventStore
import org.sollecitom.chassis.ddd.event.store.test.specification.EventStoreTestSpecification

@TestInstance(PER_CLASS)
private class InMemoryEventStoreTests : EventStoreTestSpecification, CoreDataGenerator by CoreDataGenerator.testProvider {

    override fun historicalEvents() = InMemoryEventStore()

    override fun EventStore.Mutable<Event>.forEntityId(entityId: Id) = (this as InMemoryEventStore).forEntityId(entityId)
}