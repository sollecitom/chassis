package com.element.dpg.libs.chassis.ddd.application

import com.element.dpg.libs.chassis.correlation.core.domain.access.Access
import com.element.dpg.libs.chassis.correlation.core.domain.context.InvocationContext
import com.element.dpg.libs.chassis.correlation.logging.utils.log
import com.element.dpg.libs.chassis.ddd.domain.Command
import com.element.dpg.libs.chassis.logger.core.loggable.Loggable

class LoggingApplicationAdapter(private val delegate: Application) : Application {

    context(InvocationContext<ACCESS>)
    override suspend fun <RESULT, ACCESS : Access> invoke(command: Command<RESULT, ACCESS>): RESULT {

        logger.log { "Processing command $command" }
        val result = delegate(command)
        logger.log { "Produced result $result from processing command $command" }
        return result
    }

    companion object : Loggable()
}