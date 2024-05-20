package com.element.dpg.libs.chassis.ddd.application.dispatching

import com.element.dpg.libs.chassis.correlation.core.domain.context.InvocationContext
import com.element.dpg.libs.chassis.ddd.domain.Command

interface Application {

    context(InvocationContext<ACCESS>)
    suspend operator fun <RESULT, ACCESS : _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access> invoke(command: Command<RESULT, ACCESS>): RESULT

    companion object
}