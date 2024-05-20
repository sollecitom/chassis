package com.element.dpg.libs.chassis.ddd.application.dispatching

import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import com.element.dpg.libs.chassis.correlation.logging.utils.log
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.logger.core.loggable.Loggable

class LoggingApplicationAdapter(private val delegate: Application) : Application {

    context(InvocationContext<ACCESS>)
    override suspend fun <RESULT, ACCESS : _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access> invoke(command: Command<RESULT, ACCESS>): RESULT {

        logger.log { "Processing command $command" }
        val result = delegate(command)
        logger.log { "Produced result $result from processing command $command" }
        return result
    }

    companion object : Loggable()
}