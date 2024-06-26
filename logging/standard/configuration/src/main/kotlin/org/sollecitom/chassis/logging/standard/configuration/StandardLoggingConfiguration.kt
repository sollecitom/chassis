package com.element.dpg.libs.chassis.logging.standard.configuration

import com.element.dpg.libs.chassis.logger.core.FormatLogEntry
import com.element.dpg.libs.chassis.logger.core.appender.PrintStreamAppender
import com.element.dpg.libs.chassis.logger.core.defaults.DefaultFormatToString
import com.element.dpg.libs.chassis.logger.core.loggingFunction
import com.element.dpg.libs.chassis.logger.core.loggingLevelEnabler
import com.element.dpg.libs.chassis.logger.json.formatter.DefaultFormatToJson
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.MapEnvironment

object StandardLoggingConfiguration {

    operator fun invoke(
            environment: Environment,
            minimumLoggingLevelEnvironmentVariableName: String = Properties.LOGGING_LEVEL_ENV_VARIABLE,
            minimumLoggingLevelOverridesEnvironmentVariableName: String = Properties.LOGGING_LEVEL_OVERRIDES_ENV_VARIABLE,
            logFormatEnvironmentVariableName: String = Properties.FORMAT_ENV_VARIABLE,
            defaultMinimumLoggingLevel: com.element.dpg.libs.chassis.logger.core.LoggingLevel = com.element.dpg.libs.chassis.logger.core.LoggingLevel.INFO,
            defaultMinimumLoggingLevelOverrides: Map<String, com.element.dpg.libs.chassis.logger.core.LoggingLevel> = emptyMap(),
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
            minimumLoggingLevelEnvironmentVariableName: String = Properties.LOGGING_LEVEL_ENV_VARIABLE,
            minimumLoggingLevelOverridesEnvironmentVariableName: String = Properties.LOGGING_LEVEL_OVERRIDES_ENV_VARIABLE,
            logFormatEnvironmentVariableName: String = Properties.FORMAT_ENV_VARIABLE,
            defaultMinimumLoggingLevel: com.element.dpg.libs.chassis.logger.core.LoggingLevel = com.element.dpg.libs.chassis.logger.core.LoggingLevel.INFO,
            defaultMinimumLoggingLevelOverrides: Map<String, com.element.dpg.libs.chassis.logger.core.LoggingLevel> = emptyMap(),
            defaultLogFormat: LogFormat = LogFormat.PLAIN,
            readConfigurationValue: (String) -> String? = ::defaultReadConfigurationValue
    ): LoggingCustomizer {

        val minimumLoggingLevelValue = defaultMinimumLoggingLevelFromEnvironment(minimumLoggingLevelEnvironmentVariableName, readConfigurationValue) ?: defaultMinimumLoggingLevel
        val minimumLoggingLevelOverridesValue = minimumLoggingLevelOverridesFromEnvironment(minimumLoggingLevelOverridesEnvironmentVariableName, readConfigurationValue) ?: defaultMinimumLoggingLevelOverrides
        val logFormatValue = logFormatFromEnvironment(logFormatEnvironmentVariableName, readConfigurationValue) ?: defaultLogFormat

        return CombinedLoggingCustomizer(minimumLoggingLevelValue, minimumLoggingLevelOverridesValue, logFormatValue.asFormattingFunction())
    }

    private fun defaultMinimumLoggingLevelFromEnvironment(key: String, readConfigurationValue: (String) -> String?): com.element.dpg.libs.chassis.logger.core.LoggingLevel? = readConfigurationValue(key)?.uppercase()?.let(com.element.dpg.libs.chassis.logger.core.LoggingLevel::valueOf)

    private fun minimumLoggingLevelOverridesFromEnvironment(key: String, readConfigurationValue: (String) -> String?): Map<String, com.element.dpg.libs.chassis.logger.core.LoggingLevel>? = readConfigurationValue(key)?.split(",")?.map { it.split("=") }?.associate { it.first() to com.element.dpg.libs.chassis.logger.core.LoggingLevel.valueOf(it.last()) }

    private fun logFormatFromEnvironment(key: String, readConfigurationValue: (String) -> String?): LogFormat? = readConfigurationValue(key)?.lowercase()?.let(::parseLogFormat)

    private fun LogFormat.asFormattingFunction(): FormatLogEntry<String> = when (this) {
        LogFormat.PLAIN -> DefaultFormatToString
        LogFormat.JSON -> DefaultFormatToJson
    }

    private fun parseLogFormat(value: String): LogFormat = when (value) {
        Properties.FORMAT_STRING -> LogFormat.PLAIN
        Properties.FORMAT_JSON -> LogFormat.JSON
        else -> error("Unknown log format $value")
    }

    object Properties {
        const val FORMAT_STRING = "string"
        const val FORMAT_JSON = "json"
        const val LOGGING_LEVEL_ENV_VARIABLE = "LOGGING_LEVEL_DEFAULT"
        const val LOGGING_LEVEL_OVERRIDES_ENV_VARIABLE = "LOGGING_LEVELS"
        const val FORMAT_ENV_VARIABLE = "LOGGING_FORMAT"
    }
}

private fun defaultReadConfigurationValue(key: String): String? {
    val environment = MapEnvironment.from(System.getProperties()) overrides MapEnvironment.from(System.getenv().toProperties())
    return environment[key]
}

private class CombinedLoggingCustomizer(override val minimumLoggingLevel: com.element.dpg.libs.chassis.logger.core.LoggingLevel, override val minimumLoggingLevelOverrides: Map<String, com.element.dpg.libs.chassis.logger.core.LoggingLevel>, override val format: FormatLogEntry<String>) : LoggingCustomizer {

    override fun invoke(customizer: com.element.dpg.libs.chassis.logger.core.LoggerFactory.Customizer) {
        with(customizer) {
            loggingFunction = loggingFunction {
                addAppender(PrintStreamAppender(maximumLevel = com.element.dpg.libs.chassis.logger.core.LoggingLevel.WARN, stream = System::out, format = format))
                addAppender(PrintStreamAppender(minimumLevel = com.element.dpg.libs.chassis.logger.core.LoggingLevel.ERROR, stream = System::err, format = format))
            }
            isEnabledForLoggerName = loggingLevelEnabler(defaultMinimumLoggingLevel = minimumLoggingLevel) {
                minimumLoggingLevelOverrides.forEach { (loggerName, minimumLoggingLevel) -> loggerName withMinimumLoggingLevel minimumLoggingLevel }
            }
        }
    }
}