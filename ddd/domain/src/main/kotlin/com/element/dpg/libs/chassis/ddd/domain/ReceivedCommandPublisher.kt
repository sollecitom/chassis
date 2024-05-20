package com.element.dpg.libs.chassis.ddd.domain

import com.element.dpg.libs.chassis.core.domain.lifecycle.Startable
import com.element.dpg.libs.chassis.core.domain.lifecycle.Stoppable
import com.element.dpg.libs.chassis.correlation.core.domain.context.InvocationContext

interface ReceivedCommandPublisher<in COMMAND : Command<*, ACCESS>, out ACCESS : _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access> : Startable, Stoppable {

    context(InvocationContext<ACCESS>)
    suspend fun publish(event: CommandWasReceived<COMMAND>)
}