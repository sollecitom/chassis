package com.element.dpg.libs.chassis.ddd.test.stubs

import com.element.dpg.libs.chassis.ddd.domain.Event

sealed interface GenericTestEvent : Event {

    companion object
}