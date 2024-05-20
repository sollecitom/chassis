package com.element.dpg.libs.chassis.ddd.application.dispatching.dispatching

import com.element.dpg.libs.chassis.correlation.core.domain.context.InvocationContext
import com.element.dpg.libs.chassis.ddd.domain.Command
import com.element.dpg.libs.chassis.ddd.domain.Happening

interface CommandHandler<in COMMAND : Command<RESULT, ACCESS>, out RESULT, out ACCESS : _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access> {

    val commandType: Happening.Type

    context(InvocationContext<ACCESS>)
    suspend fun process(command: COMMAND): RESULT
}