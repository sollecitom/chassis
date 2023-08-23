package org.sollecitom.chassis.http4k.utils.lens

import org.http4k.core.ContentType
import org.http4k.core.Response
import org.json.JSONArray
import org.json.JSONObject
import org.sollecitom.chassis.json.utils.serde.JsonDeserializer
import org.sollecitom.chassis.json.utils.serde.JsonSerializer

fun Response.bodyJsonObject(): JSONObject = bodyString().let(::JSONObject)

fun Response.bodyJsonArray(): JSONArray = bodyString().let(::JSONArray)

fun <VALUE : Any> Response.body(deserializer: JsonDeserializer<VALUE>): VALUE = bodyJsonObject().let(deserializer::deserialize)

fun <VALUE : Any> Response.body(value: VALUE, serializer: JsonSerializer<VALUE>) = body(serializer.serialize(value))

fun Response.header(header: HttpHeader, value: String) = header(header.name, value)
fun Response.replaceHeader(header: HttpHeader, value: String) = replaceHeader(header.name, value)

fun Response.contentType(contentType: ContentType) = replaceHeader(HttpHeaders.ContentType, contentType.toHeaderValue())