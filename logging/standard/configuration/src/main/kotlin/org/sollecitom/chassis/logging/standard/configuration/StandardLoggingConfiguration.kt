package org.sollecitom.chassis.logging.standard.configuration

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.MapEnvironment
import org.sollecitom.chassis.logger.core.*
import org.sollecitom.chassis.logger.core.appender.PrintStreamAppender
import org.sollecitom.chassis.logger.core.defaults.DefaultFormatToString
import org.sollecitom.chassis.logger.json.formatter.DefaultFormatToJson

object StandardLoggingConfiguration {

    operator fun invoke(
            environment: Environment,
            minimumLoggingLevelEnvironmentVariableName: String = Properties.defaultMinimumLoggingLevelEnvironmentVariableName,
            minimumLoggingLevelOverridesEnvironmentVariableName: String = Properties.defaultMinimumLoggingLevelOverridesEnvironmentVariableName,
            logFormatEnvironmentVariableName: String = Properties.defaultLogFormatEnvironmentVariableName,
            defaultMinimumLoggingLevel: LoggingLevel = LoggingLevel.INFO,
            defaultMinimumLoggingLevelOverrides: Map<String, LoggingLevel> = emptyMap(),
            defaultLogFormat: LogFormat = LogFormat.PLAIN
    ) = invoke(
            minimumLoggingLevelEnvironmentVariableName,
            minimumLoggingLevelOverridesEnvironmentVariableName,
            logFormatEnvironmentVariableName,
            defaultMinimumLoggingLevel,
            defaultMinimumLoggingLevelOverrides,
            defaultLogFormat,
            environment::get
    )

    operator fun invoke(
            minimumLoggingLevelEnvironmentVariableName: String = Properties.defaultMinimumLoggingLevelEnvironmentVariableName,
            minimumLoggingLevelOverridesEnvironmentVariableName: String = Properties.defaultMinimumLoggingLevelOverridesEnvironmentVariableName,
            logFormatEnvironmentVariableName: String = Properties.defaultLogFormatEnvironmentVariableName,
            defaultMinimumLoggingLevel: LoggingLevel = LoggingLevel.INFO,
            defaultMinimumLoggingLevelOverrides: Map<String, LoggingLevel> = emptyMap(),
            defaultLogFormat: LogFormat = LogFormat.PLAIN,
            readConfigurationValue: (String) -> String? = ::defaultReadConfigurationValue
    ): LoggingCustomizer {

        val minimumLoggingLevelValue = defaultMinimumLoggingLevelFromEnvironment(minimumLoggingLevelEnvironmentVariableName, readConfigurationValue) ?: defaultMinimumLoggingLevel
        val minimumLoggingLevelOverridesValue = minimumLoggingLevelOverridesFromEnvironment(minimumLoggingLevelOverridesEnvironmentVariableName, readConfigurationValue) ?: defaultMinimumLoggingLevelOverrides
        val logFormatValue = logFormatFromEnvironment(logFormatEnvironmentVariableName, readConfigurationValue) ?: defaultLogFormat

        return CombinedLoggingCustomizer(minimumLoggingLevelValue, minimumLoggingLevelOverridesValue, logFormatValue.asFormattingFunction())
    }

    private fun defaultMinimumLoggingLevelFromEnvironment(key: String, readConfigurationValue: (String) -> String?): LoggingLevel? = readConfigurationValue(key)?.uppercase()?.let(LoggingLevel::valueOf)

    private fun minimumLoggingLevelOverridesFromEnvironment(key: String, readConfigurationValue: (String) -> String?): Map<String, LoggingLevel>? = readConfigurationValue(key)?.split(",")?.map { it.split("=") }?.associate { it.first() to LoggingLevel.valueOf(it.last()) }

    private fun logFormatFromEnvironment(key: String, readConfigurationValue: (String) -> String?): LogFormat? = readConfigurationValue(key)?.lowercase()?.let(::parseLogFormat)

    private fun LogFormat.asFormattingFunction(): FormatLogEntry<String> = when (this) {
        LogFormat.PLAIN -> DefaultFormatToString
        LogFormat.JSON -> DefaultFormatToJson
    }

    private fun parseLogFormat(value: String): LogFormat = when (value) {
        Properties.stringFormatterEnabler -> LogFormat.PLAIN
        Properties.jsonFormatterEnabler -> LogFormat.JSON
        else -> error("Unknown log format $value")
    }

    object Properties {
        const val stringFormatterEnabler = "string"
        const val jsonFormatterEnabler = "json"
        const val defaultMinimumLoggingLevelEnvironmentVariableName = "LOGGING_LEVEL_DEFAULT"
        const val defaultMinimumLoggingLevelOverridesEnvironmentVariableName = "LOGGING_LEVELS"
        const val defaultLogFormatEnvironmentVariableName = "LOGGING_FORMAT"
    }
}

private fun defaultReadConfigurationValue(key: String): String? {
    val environment = MapEnvironment.from(System.getProperties()) overrides MapEnvironment.from(System.getenv().toProperties())
    return environment[key]
}

private class CombinedLoggingCustomizer(override val minimumLoggingLevel: LoggingLevel, override val minimumLoggingLevelOverrides: Map<String, LoggingLevel>, override val format: FormatLogEntry<String>) : LoggingCustomizer {

    override fun invoke(customizer: LoggerFactory.Customizer) {
        with(customizer) {
            loggingFunction = loggingFunction {
                addAppender(PrintStreamAppender(maximumLevel = LoggingLevel.WARN, stream = System::out, format = format))
                addAppender(PrintStreamAppender(minimumLevel = LoggingLevel.ERROR, stream = System::err, format = format))
            }
            isEnabledForLoggerName = loggingLevelEnabler(defaultMinimumLoggingLevel = minimumLoggingLevel) {
                minimumLoggingLevelOverrides.forEach { (loggerName, minimumLoggingLevel) -> loggerName withMinimumLoggingLevel minimumLoggingLevel }
            }
        }
    }
}