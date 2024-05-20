package com.element.dpg.libs.chassis.correlation.logging.test.utils

import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import org.json.JSONObject
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.serialization.json.context.jsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getJSONObjectOrNull
import org.sollecitom.chassis.kotlin.extensions.text.removeFromLast

fun Assert<List<String>>.haveContext(invocationContext: InvocationContext<*>, allowEmpty: Boolean = false) = given { logs ->

    if (!allowEmpty) {
        assertThat(logs).isNotEmpty()
    }
    logs.forEach { logLine ->
        val context = extractInvocationContext(logLine)
        assertk.assertThat(context).isNotNull().isEqualTo(invocationContext)
    }
}

private fun extractInvocationContext(logLine: String): InvocationContext<*>? {

    return logLine.runCatching { JSONObject(this) }.map { json ->
        json.getJSONObjectOrNull("context")?.getJSONObjectOrNull("invocation")?.let(InvocationContext.jsonSerde::deserialize)
    }.getOrElse {
        val rawContext = logLine.removeFromLast(" - context: ").takeUnless { it == logLine }?.let { logLine.removePrefix(it).removePrefix(" - context: ") }
        rawContext?.let { JSONObject(it).getJSONObjectOrNull("invocation")?.let(InvocationContext.jsonSerde::deserialize) }
    }
}