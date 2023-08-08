package org.sollecitom.chassis.example.service.endpoint.write.application

import org.sollecitom.chassis.logger.core.loggable.Loggable

internal class LoggingApplicationAdapter(private val delegate: Application) : Application {

    override suspend fun <RESULT> invoke(command: ApplicationCommand<RESULT>): RESULT {

        logger.debug { "Processing command $command" }
        val result = delegate(command)
        logger.debug { "Produced result $result from processing command $command" }
        return result
    }

    companion object : Loggable()
}