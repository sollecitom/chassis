package com.element.dpg.libs.chassis.ddd.domain.hexagonal

import com.element.dpg.libs.chassis.core.domain.lifecycle.Startable
import com.element.dpg.libs.chassis.core.domain.lifecycle.Stoppable
import com.element.dpg.libs.chassis.core.domain.networking.Port

interface DrivingAdapter : Startable, Stoppable {

    interface WithPortBinding : DrivingAdapter {

        val port: Port
    }
}