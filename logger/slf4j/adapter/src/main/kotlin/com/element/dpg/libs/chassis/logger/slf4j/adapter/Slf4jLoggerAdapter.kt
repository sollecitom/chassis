@file:Suppress("UsePropertyAccessSyntax")

package com.element.dpg.libs.chassis.logger.slf4j.adapter

import com.element.dpg.libs.chassis.logger.core.Logger
import org.slf4j.Marker
import org.slf4j.helpers.MessageFormatter
import org.slf4j.Logger as Slf4jLogger

class Slf4jLoggerAdapter(private val logger: Logger) : Slf4jLogger {

    override fun getName() = logger.name

    override fun isTraceEnabled() = with(logger) { com.element.dpg.libs.chassis.logger.core.LoggingLevel.TRACE.isEnabledForLoggerName(logger.name) }
    override fun isTraceEnabled(marker: Marker?) = isTraceEnabled()
    override fun trace(msg: String) = logger.trace { msg }
    override fun trace(format: String, arg: Any?) = logger.trace { format(format, arg) }
    override fun trace(format: String, arg1: Any?, arg2: Any?) = logger.trace { format(format, arg1, arg2) }
    override fun trace(format: String, vararg arguments: Any?) = logger.trace { format(format, *arguments) }
    override fun trace(msg: String, t: Throwable?) = logger.trace(t) { msg }
    override fun trace(marker: Marker?, msg: String) = logger.trace { msg }
    override fun trace(marker: Marker?, format: String, arg: Any?) = trace(format, arg)
    override fun trace(marker: Marker?, format: String, arg1: Any?, arg2: Any?) = trace(format, arg1, arg2)
    override fun trace(marker: Marker?, format: String, vararg argArray: Any?) = trace(format, *argArray)
    override fun trace(marker: Marker?, msg: String, t: Throwable?) = trace(msg, t)

    override fun isDebugEnabled() = with(logger) { com.element.dpg.libs.chassis.logger.core.LoggingLevel.DEBUG.isEnabledForLoggerName(logger.name) }
    override fun isDebugEnabled(marker: Marker?) = isDebugEnabled()
    override fun debug(msg: String) = logger.debug { msg }
    override fun debug(format: String, arg: Any?) = logger.debug { format(format, arg) }
    override fun debug(format: String, arg1: Any?, arg2: Any?) = logger.debug { format(format, arg1, arg2) }
    override fun debug(format: String, vararg arguments: Any?) = logger.debug { format(format, *arguments) }
    override fun debug(msg: String, t: Throwable?) = logger.debug(t) { msg }
    override fun debug(marker: Marker?, msg: String) = logger.debug { msg }
    override fun debug(marker: Marker?, format: String, arg: Any?) = debug(format, arg)
    override fun debug(marker: Marker?, format: String, arg1: Any?, arg2: Any?) = debug(format, arg1, arg2)
    override fun debug(marker: Marker?, format: String, vararg argArray: Any?) = debug(format, *argArray)
    override fun debug(marker: Marker?, msg: String, t: Throwable?) = debug(msg, t)

    override fun isInfoEnabled() = with(logger) { com.element.dpg.libs.chassis.logger.core.LoggingLevel.INFO.isEnabledForLoggerName(logger.name) }
    override fun isInfoEnabled(marker: Marker?) = isInfoEnabled()
    override fun info(msg: String) = logger.info { msg }
    override fun info(format: String, arg: Any?) = logger.info { format(format, arg) }
    override fun info(format: String, arg1: Any?, arg2: Any?) = logger.info { format(format, arg1, arg2) }
    override fun info(format: String, vararg arguments: Any?) = logger.info { format(format, *arguments) }
    override fun info(msg: String, t: Throwable?) = logger.info(t) { msg }
    override fun info(marker: Marker?, msg: String) = logger.info { msg }
    override fun info(marker: Marker?, format: String, arg: Any?) = info(format, arg)
    override fun info(marker: Marker?, format: String, arg1: Any?, arg2: Any?) = info(format, arg1, arg2)
    override fun info(marker: Marker?, format: String, vararg argArray: Any?) = info(format, *argArray)
    override fun info(marker: Marker?, msg: String, t: Throwable?) = info(msg, t)

    override fun isWarnEnabled() = with(logger) { com.element.dpg.libs.chassis.logger.core.LoggingLevel.WARN.isEnabledForLoggerName(logger.name) }
    override fun isWarnEnabled(marker: Marker?) = isWarnEnabled()
    override fun warn(msg: String) = logger.warn { msg }
    override fun warn(format: String, arg: Any?) = logger.warn { format(format, arg) }
    override fun warn(format: String, arg1: Any?, arg2: Any?) = logger.warn { format(format, arg1, arg2) }
    override fun warn(format: String, vararg arguments: Any?) = logger.warn { format(format, *arguments) }
    override fun warn(msg: String, t: Throwable?) = logger.warn(t) { msg }
    override fun warn(marker: Marker?, msg: String) = logger.warn { msg }
    override fun warn(marker: Marker?, format: String, arg: Any?) = warn(format, arg)
    override fun warn(marker: Marker?, format: String, arg1: Any?, arg2: Any?) = warn(format, arg1, arg2)
    override fun warn(marker: Marker?, format: String, vararg argArray: Any?) = warn(format, *argArray)
    override fun warn(marker: Marker?, msg: String, t: Throwable?) = warn(msg, t)

    override fun isErrorEnabled() = with(logger) { com.element.dpg.libs.chassis.logger.core.LoggingLevel.ERROR.isEnabledForLoggerName(logger.name) }
    override fun isErrorEnabled(marker: Marker?) = isErrorEnabled()
    override fun error(msg: String) = logger.error { msg }
    override fun error(format: String, arg: Any?) = logger.error { format(format, arg) }
    override fun error(format: String, arg1: Any?, arg2: Any?) = logger.error { format(format, arg1, arg2) }
    override fun error(format: String, vararg arguments: Any?) = logger.error { format(format, *arguments) }
    override fun error(msg: String, t: Throwable?) = logger.error(t) { msg }
    override fun error(marker: Marker?, msg: String) = logger.error { msg }
    override fun error(marker: Marker?, format: String, arg: Any?) = error(format, arg)
    override fun error(marker: Marker?, format: String, arg1: Any?, arg2: Any?) = error(format, arg1, arg2)
    override fun error(marker: Marker?, format: String, vararg argArray: Any?) = error(format, *argArray)
    override fun error(marker: Marker?, msg: String, t: Throwable?) = error(msg, t)

    private fun format(template: String, vararg args: Any?): String = MessageFormatter.arrayFormat(template, args).message
}

fun Logger.asSlf4jLogger(): Slf4jLogger = Slf4jLoggerAdapter(this)