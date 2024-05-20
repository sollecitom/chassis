package com.element.dpg.libs.chassis.http4k.utils.lens

import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonDeserializer
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.lens.BiDiBodyLensSpec
import org.http4k.lens.BodyLensSpec
import org.http4k.lens.ContentNegotiation
import org.http4k.lens.string
import org.json.JSONArray
import org.json.JSONObject

fun Body.Companion.jsonObject(description: String? = null, contentNegotiation: ContentNegotiation = ContentNegotiation.StrictNoDirective): BiDiBodyLensSpec<JSONObject> = string(ContentType.APPLICATION_JSON, description, contentNegotiation).map(::JSONObject, JSONObject::toString)

fun Body.Companion.jsonArray(description: String? = null, contentNegotiation: ContentNegotiation = ContentNegotiation.StrictNoDirective): BiDiBodyLensSpec<JSONArray> = string(ContentType.APPLICATION_JSON, description, contentNegotiation).map(::JSONArray, JSONArray::toString)

fun <VALUE : Any> BiDiBodyLensSpec<JSONObject>.map(serde: JsonSerde<VALUE>): BiDiBodyLensSpec<VALUE> = map(serde::deserialize, serde::serialize)

fun <VALUE : Any> BodyLensSpec<JSONObject>.map(deserializer: JsonDeserializer<VALUE>): BodyLensSpec<VALUE> = map(deserializer::deserialize)