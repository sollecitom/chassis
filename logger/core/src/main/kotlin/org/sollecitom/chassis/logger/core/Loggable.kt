package org.sollecitom.chassis.logger.core

open class Loggable(private val loggerFactory: LoggerFactory = JvmLoggerFactory) : LoggableType {

    override val logger: Logger = logger()

    final override fun logger(): Logger = loggerFactory.forLoggable(this)

    override fun logger(name: String): Logger = loggerFactory.logger(name)
}