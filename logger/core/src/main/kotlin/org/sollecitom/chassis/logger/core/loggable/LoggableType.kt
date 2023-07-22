package org.sollecitom.chassis.logger.core.loggable

import org.sollecitom.chassis.logger.core.Logger

interface LoggableType {

    val logger: Logger

    fun logger(): Logger

    fun logger(name: String): Logger
}