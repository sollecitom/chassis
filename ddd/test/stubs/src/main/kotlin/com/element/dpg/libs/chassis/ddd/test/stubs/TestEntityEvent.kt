package com.element.dpg.libs.chassis.ddd.test.stubs

import com.element.dpg.libs.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.domain.versioning.IntVersion
import com.element.dpg.libs.chassis.ddd.domain.EntityEvent
import com.element.dpg.libs.chassis.ddd.domain.Event
import com.element.dpg.libs.chassis.ddd.domain.Happening
import kotlinx.datetime.Instant

data class TestEntityEvent(override val entityId: Id, override val id: Id, override val timestamp: Instant, override val context: Event.Context) : GenericTestEvent, EntityEvent {

    override val entityType = "test-entity".let(::Name)

    override val type: Happening.Type get() = Companion.type

    companion object {
        val type = Happening.Type("test-entity-event".let(::Name), 1.let(::IntVersion))
    }
}