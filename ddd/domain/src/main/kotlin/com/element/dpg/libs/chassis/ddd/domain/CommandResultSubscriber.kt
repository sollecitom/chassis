package com.element.dpg.libs.chassis.ddd.domain

import com.element.dpg.libs.chassis.core.domain.lifecycle.Startable
import com.element.dpg.libs.chassis.core.domain.lifecycle.Stoppable
import com.element.dpg.libs.chassis.correlation.core.domain.access.Access
import com.element.dpg.libs.chassis.correlation.core.domain.context.InvocationContext
import kotlinx.coroutines.Deferred

interface CommandResultSubscriber : Startable, Stoppable {

    context(InvocationContext<ACCESS>)
    fun <COMMAND : Command<RESULT, ACCESS>, RESULT : Any, ACCESS : Access> resultForCommand(event: CommandWasReceived<COMMAND>): Deferred<RESULT>
}