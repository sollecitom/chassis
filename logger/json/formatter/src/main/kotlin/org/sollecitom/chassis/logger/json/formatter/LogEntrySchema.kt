package org.sollecitom.chassis.logger.json.formatter

import org.everit.json.schema.Schema
import org.everit.json.schema.loader.SchemaClient
import org.everit.json.schema.loader.SchemaLoader
import org.json.JSONObject
import org.json.JSONTokener
import org.sollecitom.chassis.logger.core.LogEntry
import org.sollecitom.chassis.resource.utils.ResourceLoader.openAsStream

private val logEntrySchema: Schema by lazy { jsonSchemaAt("LogEntry.json") }

val LogEntry.Companion.jsonSchema: Schema get() = logEntrySchema

private fun jsonSchemaAt(location: String, schemasFolder: String? = null): Schema = openAsStream(location).use {
    val definition = JSONObject(JSONTokener(it))
    SchemaLoader.builder().schemaClient(SchemaClient.classPathAwareClient()).apply { if (schemasFolder != null) resolutionScope("classpath://$schemasFolder") }.schemaJson(definition).build().load().build()
}
