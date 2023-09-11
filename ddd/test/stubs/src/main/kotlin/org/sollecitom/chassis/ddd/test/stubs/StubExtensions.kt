package org.sollecitom.chassis.ddd.test.stubs

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.CoreDataGenerator

context(CoreDataGenerator)
fun testEvents(): Sequence<TestEvent> = sequence {
    while (true) {
        yield(testEvent())
    }
}

context(CoreDataGenerator)
fun testEntityEvents(entityId: Id? = null): Sequence<TestEntityEvent> = sequence {
    while (true) {
        yield(testEntityEvent(entityId = entityId ?: newId.internal()))
    }
}

context(CoreDataGenerator)
fun testEvent(id: Id = newId.internal(), timestamp: Instant = clock.now()) = TestEvent(id, timestamp)

context(CoreDataGenerator)
fun testEntityEvent(entityId: Id = newId.internal(), id: Id = newId.internal(), timestamp: Instant = clock.now()) = TestEntityEvent(entityId, id, timestamp)