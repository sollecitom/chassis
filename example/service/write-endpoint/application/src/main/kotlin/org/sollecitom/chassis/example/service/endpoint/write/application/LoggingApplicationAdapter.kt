package org.sollecitom.chassis.example.service.endpoint.write.application

import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.logger.core.loggable.Loggable

internal class LoggingApplicationAdapter(private val delegate: Application) : Application {

    override suspend fun <RESULT, ACCESS : Access> invoke(command: ApplicationCommand<RESULT, ACCESS>, context: InvocationContext<ACCESS>): RESULT {

        logger.debug { "Processing command $command" }
        val result = delegate(command, context)
        logger.debug { "Produced result $result from processing command $command" }
        return result
    }

    companion object : Loggable()
}