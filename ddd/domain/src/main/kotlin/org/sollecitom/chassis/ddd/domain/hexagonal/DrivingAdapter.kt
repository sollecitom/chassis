package org.sollecitom.chassis.ddd.domain.hexagonal

import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.core.domain.networking.Port

interface DrivingAdapter : Startable, Stoppable {

    interface WithPortBinding : DrivingAdapter {

        val port: Port
    }
}