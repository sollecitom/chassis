package org.sollecitom.chassis.ddd.application

import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.logging.utils.log
import org.sollecitom.chassis.logger.core.loggable.Loggable

class LoggingApplicationAdapter(private val delegate: Application) : Application {

    context(InvocationContext<ACCESS>)
    override suspend fun <RESULT, ACCESS : Access> invoke(command: ApplicationCommand<RESULT, ACCESS>): RESULT {

        logger.log { "Processing command $command" }
        val result = delegate(command)
        logger.log { "Produced result $result from processing command $command" }
        return result
    }

    companion object : Loggable()
}