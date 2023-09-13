package org.sollecitom.chassis.ddd.test.stubs

import org.sollecitom.chassis.ddd.domain.Event

sealed interface GenericTestEvent : Event {

    companion object
}