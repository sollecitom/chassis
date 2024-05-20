package com.element.dpg.libs.chassis.ddd.test.stubs

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.domain.versioning.IntVersion
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.Happening

data class TestEvent(override val id: Id, override val timestamp: Instant, override val context: Event.Context) : GenericTestEvent {

    override val type: Happening.Type get() = Companion.type

    companion object {
        val type = Happening.Type("test-event".let(::Name), 1.let(::IntVersion))
    }
}