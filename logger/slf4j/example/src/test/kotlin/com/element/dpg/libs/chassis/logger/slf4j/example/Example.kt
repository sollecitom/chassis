package com.element.dpg.libs.chassis.logger.slf4j.example

import kotlinx.coroutines.runBlocking
import com.element.dpg.libs.chassis.logger.core.JvmLoggerFactory
import com.element.dpg.libs.chassis.logger.core.LoggingLevel.*
import com.element.dpg.libs.chassis.logger.core.appender.PrintStreamAppender
import com.element.dpg.libs.chassis.logger.core.defaults.DefaultFormatToString
import com.element.dpg.libs.chassis.logger.core.loggingFunction
import com.element.dpg.libs.chassis.logger.core.loggingLevelEnabler
import com.element.dpg.libs.chassis.logger.core.withCoroutineLoggingContext

fun main(): Unit = runBlocking {

    JvmLoggerFactory.configure {
        loggingFunction = loggingFunction {
            addAppender(PrintStreamAppender(maximumLevel = WARN, stream = System::out, format = DefaultFormatToString))
            addAppender(PrintStreamAppender(minimumLevel = ERROR, stream = System::err, format = DefaultFormatToString))
        }
        isEnabledForLoggerName = loggingLevelEnabler(defaultMinimumLoggingLevel = TRACE) {
            "com.element.dpg.libs.chassis.logger.slf4j" withMinimumLoggingLevel INFO
            "com.element.dpg.libs.chassis.logger.slf4j.example" withMinimumLoggingLevel ERROR
            "com.element.dpg.libs.chassis.logger.slf4j.abc" withMinimumLoggingLevel INFO
            "com.element.dpg.libs.chassis.logger.slf4j.sl" withMinimumLoggingLevel INFO
        }
    }

    val repository: PersonRepository = EmptyPersonRepository()
    val nonexistentPersonId = "1234"

    withCoroutineLoggingContext(mapOf("actorId" to "2345", "traceId" to "3456")) {
        repository.findById(nonexistentPersonId)
        repository.findById("")
    }
}