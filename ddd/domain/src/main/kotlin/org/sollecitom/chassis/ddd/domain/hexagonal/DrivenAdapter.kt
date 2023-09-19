package org.sollecitom.chassis.ddd.domain.hexagonal

import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable
import org.sollecitom.chassis.core.domain.networking.Port

interface DrivenAdapter<PORT : Any> : Startable, Stoppable {

    val port: PORT
}

interface DrivingAdapter : Startable, Stoppable {

    interface WithPortBinding : DrivingAdapter {

        val port: Port // TODO should this be 1+ bindings instead?
    }
}