package org.sollecitom.chassis.ddd.test.stubs

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.core.utils.TimeGenerator
import org.sollecitom.chassis.core.utils.UniqueIdGenerator
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.test.utils.create

context(UniqueIdGenerator, TimeGenerator)
fun testEvents(): Sequence<TestEvent> = sequence {
    while (true) {
        yield(testEvent())
    }
}

context(UniqueIdGenerator, TimeGenerator)
fun testEntityEvents(entityId: Id? = null): Sequence<TestEntityEvent> = sequence {
    while (true) {
        yield(testEntityEvent(entityId = entityId ?: newId.internal()))
    }
}

context(UniqueIdGenerator, TimeGenerator)
fun testEvent(id: Id = newId.internal(), timestamp: Instant = clock.now(), context: Event.Context = Event.Context.create()) = TestEvent(id, timestamp, context)

context(UniqueIdGenerator, TimeGenerator)
fun testEntityEvent(entityId: Id = newId.internal(), id: Id = newId.internal(), timestamp: Instant = clock.now(), context: Event.Context = Event.Context.create()) = TestEntityEvent(entityId, id, timestamp, context)