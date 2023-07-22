package org.sollecitom.chassis.logger.core

interface LoggableType {

    val logger: Logger

    fun logger(): Logger

    fun logger(name: String): Logger
}