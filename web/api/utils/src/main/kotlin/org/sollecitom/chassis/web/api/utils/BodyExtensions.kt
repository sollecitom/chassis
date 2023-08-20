package org.sollecitom.chassis.web.api.utils

import org.http4k.core.ContentType
import org.http4k.core.Request
import org.http4k.core.Response
import org.json.JSONObject

fun Request.body(json: JSONObject) = body(json.toString()).header(HttpHeaders.ContentType.name, ContentType.APPLICATION_JSON.toHeaderValue())

fun Response.body(json: JSONObject) = body(json.toString()).header(HttpHeaders.ContentType.name, ContentType.APPLICATION_JSON.toHeaderValue())