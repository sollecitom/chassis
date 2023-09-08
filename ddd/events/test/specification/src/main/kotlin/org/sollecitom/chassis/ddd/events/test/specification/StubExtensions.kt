package org.sollecitom.chassis.ddd.events.test.specification

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.CoreDataGenerator

context(CoreDataGenerator)
internal fun testEvents(): Sequence<TestEvent> = sequence {
    while (true) {
        yield(testEvent())
    }
}

context(CoreDataGenerator)
internal fun testEntityEvents(entityId: Id? = null): Sequence<TestEntityEvent> = sequence {
    while (true) {
        yield(testEntityEvent(entityId = entityId ?: newId.internal()))
    }
}

context(CoreDataGenerator)
internal fun testEvent(id: Id = newId.internal(), timestamp: Instant = clock.now()) = TestEvent(id, timestamp)

context(CoreDataGenerator)
internal fun testEntityEvent(entityId: Id = newId.internal(), id: Id = newId.internal(), timestamp: Instant = clock.now()) = TestEntityEvent(entityId, id, timestamp)