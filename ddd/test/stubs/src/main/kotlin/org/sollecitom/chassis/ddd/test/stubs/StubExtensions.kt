package org.sollecitom.chassis.ddd.test.stubs

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.test.utils.create

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
fun testEvent(id: Id = newId.internal(), timestamp: Instant = clock.now(), context: Event.Context = Event.Context.create()) = TestEvent(id, timestamp, context)

context(CoreDataGenerator)
fun testEntityEvent(entityId: Id = newId.internal(), id: Id = newId.internal(), timestamp: Instant = clock.now(), context: Event.Context = Event.Context.create()) = TestEntityEvent(entityId, id, timestamp, context)