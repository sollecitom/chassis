package com.element.dpg.libs.chassis.http4k.utils.lens

import org.http4k.core.ContentType
import org.http4k.core.Request
import org.http4k.lens.Header
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerializer

val Request.contentType: ContentType? get() = Header.CONTENT_TYPE(this)

fun <VALUE : Any> Request.body(value: VALUE, serializer: JsonSerializer<VALUE>) = body(serializer.serialize(value))

fun Request.header(header: HttpHeader, value: String) = header(header.name, value)
fun Request.replaceHeader(header: HttpHeader, value: String) = replaceHeader(header.name, value)

fun Request.contentType(contentType: ContentType) = replaceHeader(HttpHeaders.ContentType, contentType.toHeaderValue())

fun Request.contentLength(contentLength: Int) = contentLength(contentLength.toLong())
fun Request.contentLength(contentLength: Long) = replaceHeader(HttpHeaders.ContentLength, contentLength.toString())