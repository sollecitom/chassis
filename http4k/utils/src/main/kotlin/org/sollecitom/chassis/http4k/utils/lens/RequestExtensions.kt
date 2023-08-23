package org.sollecitom.chassis.http4k.utils.lens

import org.http4k.core.ContentType
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.lens.Header
import org.sollecitom.chassis.json.utils.serde.JsonDeserializer
import org.sollecitom.chassis.json.utils.serde.JsonSerializer

val Request.contentType: ContentType? get() = Header.CONTENT_TYPE(this)

fun <VALUE : Any> Request.body(value: VALUE, serializer: JsonSerializer<VALUE>) = body(serializer.serialize(value))

fun Request.header(header: HttpHeader, value: String) = header(header.name, value)

fun Request.contentType(contentType: ContentType) = header(HttpHeaders.ContentType, contentType.toHeaderValue())

fun Request.contentLength(contentLength: Int) = contentLength(contentLength.toLong())
fun Request.contentLength(contentLength: Long) = header(HttpHeaders.ContentLength, contentLength.toString())