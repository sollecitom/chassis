package com.element.dpg.libs.chassis.logger.json.formatter

import com.element.dpg.libs.chassis.json.utils.serde.Schema
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import org.sollecitom.chassis.logger.core.LogEntry

private val logEntrySchema by lazy { jsonSchemaAt("LogEntry.json") }

val LogEntry.Companion.jsonSchema: Schema get() = logEntrySchema
