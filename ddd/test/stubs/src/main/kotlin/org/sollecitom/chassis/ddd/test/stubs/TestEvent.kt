package org.sollecitom.chassis.ddd.test.stubs

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.ddd.domain.Event

data class TestEvent(override val id: Id, override val timestamp: Instant, override val context: Event.Context) : Event {

    override val type: Event.Type get() = Type

    object Type : Event.Type {
        override val name = "test-event".let(::Name)
        override val version = 1.let(::IntVersion)
    }
}