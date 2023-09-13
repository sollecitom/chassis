package org.sollecitom.chassis.logger.json.formatter

import org.sollecitom.chassis.json.utils.Schema
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.logger.core.LogEntry

private val logEntrySchema by lazy { jsonSchemaAt("LogEntry.json") }

val LogEntry.Companion.jsonSchema: Schema get() = logEntrySchema
