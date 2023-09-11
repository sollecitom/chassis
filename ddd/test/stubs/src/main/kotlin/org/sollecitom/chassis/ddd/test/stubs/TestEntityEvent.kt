package org.sollecitom.chassis.ddd.test.stubs

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.ddd.domain.EntityEvent

data class TestEntityEvent(override val entityId: Id, override val id: Id, override val timestamp: Instant) : EntityEvent {

    override val entityType = "test-entity".let(::Name)

    override val type: EntityEvent.Type get() = Type

    object Type : EntityEvent.Type {
        override val name = "test-event".let(::Name)
        override val version = 1.let(::IntVersion)
    }
}