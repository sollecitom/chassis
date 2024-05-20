package com.element.dpg.libs.chassis.ddd.application

import com.element.dpg.libs.chassis.correlation.core.domain.access.Access
import com.element.dpg.libs.chassis.correlation.core.domain.context.InvocationContext
import com.element.dpg.libs.chassis.ddd.domain.Command

interface Application {

    context(InvocationContext<ACCESS>)
    suspend operator fun <RESULT, ACCESS : Access> invoke(command: Command<RESULT, ACCESS>): RESULT

    companion object
}