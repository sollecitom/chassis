package org.sollecitom.chassis.logger.json.formatter

import com.github.erosb.jsonsKema.Schema
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.logger.core.LogEntry

private val logEntrySchema: Schema by lazy { jsonSchemaAt("LogEntry.json") }

val LogEntry.Companion.jsonSchema: Schema get() = logEntrySchema
