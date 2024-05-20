package com.element.dpg.libs.chassis.http4k.utils.lens

import com.element.dpg.libs.chassis.json.utils.serde.JsonDeserializer
import com.element.dpg.libs.chassis.json.utils.serde.JsonSerializer
import org.http4k.core.ContentType
import org.http4k.core.Response
import org.http4k.filter.ResponseFilters
import org.json.JSONArray
import org.json.JSONObject

fun Response.bodyJsonObject(): JSONObject = bodyString().let(::JSONObject)

fun Response.bodyJsonArray(): JSONArray = bodyString().let(::JSONArray)

fun <VALUE : Any> Response.body(deserializer: JsonDeserializer<VALUE>): VALUE = bodyJsonObject().let(deserializer::deserialize)

fun <VALUE : Any> Response.body(value: VALUE, serializer: JsonSerializer<VALUE>) = body(serializer.serialize(value))

fun Response.header(header: HttpHeader, value: String) = header(header.name, value)
fun Response.replaceHeader(header: HttpHeader, value: String) = replaceHeader(header.name, value)

fun Response.contentType(contentType: ContentType) = replaceHeader(HttpHeaders.ContentType, contentType.toHeaderValue())

fun Response.contentLength(length: Int) = contentLength(length.toLong())
fun Response.contentLength(length: Long) = replaceHeader(HttpHeaders.ContentLength, length.toString())

val ResponseFilters.AddContentLength get() = Modify({ response -> response.body.length?.takeUnless { it == 0L }?.let { length -> response.replaceHeader(HttpHeaders.ContentLength, length.toString()) } ?: response })