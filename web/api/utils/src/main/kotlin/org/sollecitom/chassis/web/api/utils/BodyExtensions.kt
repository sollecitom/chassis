package org.sollecitom.chassis.web.api.utils

import org.http4k.core.ContentType
import org.http4k.core.Request
import org.http4k.core.Response
import org.json.JSONObject

fun Request.body(json: JSONObject) = body(json.toString()).contentType(ContentType.APPLICATION_JSON)

fun Response.body(json: JSONObject) = body(json.toString()).contentType(ContentType.APPLICATION_JSON)

fun Request.contentType(contentType: ContentType) = header(HttpHeaders.ContentType.name, contentType.toHeaderValue())

fun Response.contentType(contentType: ContentType) = header(HttpHeaders.ContentType.name, contentType.toHeaderValue())