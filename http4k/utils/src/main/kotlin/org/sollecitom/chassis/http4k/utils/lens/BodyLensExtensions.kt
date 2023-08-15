package org.sollecitom.chassis.http4k.utils.lens

import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.lens.BiDiBodyLensSpec
import org.http4k.lens.ContentNegotiation
import org.http4k.lens.string
import org.json.JSONArray
import org.json.JSONObject

fun Body.Companion.jsonObject(description: String? = null, contentNegotiation: ContentNegotiation = ContentNegotiation.None): BiDiBodyLensSpec<JSONObject> = string(ContentType.APPLICATION_JSON, description, contentNegotiation).map(::JSONObject, JSONObject::toString)

fun Body.Companion.jsonArray(description: String? = null, contentNegotiation: ContentNegotiation = ContentNegotiation.None): BiDiBodyLensSpec<JSONArray> = string(ContentType.APPLICATION_JSON, description, contentNegotiation).map(::JSONArray, JSONArray::toString)