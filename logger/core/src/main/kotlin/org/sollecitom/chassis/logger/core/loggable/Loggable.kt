package org.sollecitom.chassis.logger.core.loggable

import org.sollecitom.chassis.logger.core.JvmLoggerFactory
import org.sollecitom.chassis.logger.core.Logger
import org.sollecitom.chassis.logger.core.LoggerFactory

open class Loggable(private val loggerFactory: LoggerFactory = JvmLoggerFactory) : LoggableType {

    override val logger: Logger = logger()

    final override fun logger(): Logger = loggerFactory.forLoggable(this)

    override fun logger(name: String): Logger = loggerFactory.logger(name)
}