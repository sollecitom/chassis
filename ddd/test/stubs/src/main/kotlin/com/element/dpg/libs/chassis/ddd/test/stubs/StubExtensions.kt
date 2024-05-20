package com.element.dpg.libs.chassis.ddd.test.stubs

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.utils.TimeGenerator
import com.element.dpg.libs.chassis.core.utils.UniqueIdGenerator
import com.element.dpg.libs.chassis.ddd.domain.Event
import com.element.dpg.libs.chassis.ddd.test.utils.create
import kotlinx.datetime.Instant

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