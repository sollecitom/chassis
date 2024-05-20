package com.element.dpg.libs.chassis.logger.core.loggable

import com.element.dpg.libs.chassis.logger.core.JvmLoggerFactory
import com.element.dpg.libs.chassis.logger.core.Logger
import com.element.dpg.libs.chassis.logger.core.LoggerFactory

open class Loggable(private val loggerFactory: com.element.dpg.libs.chassis.logger.core.LoggerFactory = JvmLoggerFactory) : LoggableType {

    override val logger: Logger = logger()

    final override fun logger(): Logger = loggerFactory.forLoggable(this)

    override fun logger(name: String): Logger = loggerFactory.logger(name)
}