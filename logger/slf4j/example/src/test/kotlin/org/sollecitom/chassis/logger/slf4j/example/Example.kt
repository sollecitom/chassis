package org.sollecitom.chassis.logger.slf4j.example

import kotlinx.coroutines.runBlocking
import org.sollecitom.chassis.logger.core.JvmLoggerFactory
import org.sollecitom.chassis.logger.core.LoggingLevel.*
import org.sollecitom.chassis.logger.core.appender.PrintStreamAppender
import org.sollecitom.chassis.logger.core.defaults.DefaultFormatToString
import org.sollecitom.chassis.logger.core.loggingFunction
import org.sollecitom.chassis.logger.core.loggingLevelEnabler
import org.sollecitom.chassis.logger.core.withCoroutineLoggingContext

fun main(): Unit = runBlocking {

    JvmLoggerFactory.configure {
        loggingFunction = loggingFunction {
            addAppender(PrintStreamAppender(maximumLevel = WARN, stream = System::out, format = DefaultFormatToString))
            addAppender(PrintStreamAppender(minimumLevel = ERROR, stream = System::err, format = DefaultFormatToString))
        }
        isEnabledForLoggerName = loggingLevelEnabler(defaultMinimumLoggingLevel = TRACE) {
            "org.sollecitom.chassis.logger.slf4j" withMinimumLoggingLevel INFO
            "org.sollecitom.chassis.logger.slf4j.example" withMinimumLoggingLevel ERROR
            "org.sollecitom.chassis.logger.slf4j.abc" withMinimumLoggingLevel INFO
            "org.sollecitom.chassis.logger.slf4j.sl" withMinimumLoggingLevel INFO
        }
    }

    val repository: PersonRepository = EmptyPersonRepository()
    val nonexistentPersonId = "1234"

    withCoroutineLoggingContext(mapOf("actorId" to "2345", "traceId" to "3456")) {
        repository.findById(nonexistentPersonId)
        repository.findById("")
    }
}