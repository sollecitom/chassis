package com.element.dpg.libs.chassis.logger.core.loggable

import com.element.dpg.libs.chassis.logger.core.Logger

interface LoggableType {

    val logger: Logger

    fun logger(): Logger

    fun logger(name: String): Logger
}