package com.element.dpg.libs.chassis.ddd.domain.hexagonal

import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable

interface DrivenAdapter<PORT : Any> : Startable, Stoppable {

    val port: PORT
}