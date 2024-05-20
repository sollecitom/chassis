package com.element.dpg.libs.chassis.ddd.domain.hexagonal

import com.element.dpg.libs.chassis.core.domain.lifecycle.Startable
import com.element.dpg.libs.chassis.core.domain.lifecycle.Stoppable

interface DrivenAdapter<PORT : Any> : Startable, Stoppable {

    val port: PORT
}