package org.sollecitom.chassis.http4k.utils.lens

import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.lens.BiDiBodyLensSpec
import org.http4k.lens.BodyLensSpec
import org.http4k.lens.ContentNegotiation
import org.http4k.lens.string
import org.json.JSONArray
import org.json.JSONObject
import org.sollecitom.chassis.json.utils.serde.JsonDeserializer
import org.sollecitom.chassis.json.utils.serde.JsonSerde

fun Body.Companion.jsonObject(description: String? = null, contentNegotiation: ContentNegotiation = ContentNegotiation.NonStrict): BiDiBodyLensSpec<JSONObject> = string(ContentType.APPLICATION_JSON, description, contentNegotiation).map(::JSONObject, JSONObject::toString)

fun Body.Companion.jsonArray(description: String? = null, contentNegotiation: ContentNegotiation = ContentNegotiation.NonStrict): BiDiBodyLensSpec<JSONArray> = string(ContentType.APPLICATION_JSON, description, contentNegotiation).map(::JSONArray, JSONArray::toString)

fun <VALUE : Any> BiDiBodyLensSpec<JSONObject>.map(serde: JsonSerde<VALUE>): BiDiBodyLensSpec<VALUE> = map(serde::deserialize, serde::serialize)

fun <VALUE : Any> BodyLensSpec<JSONObject>.map(deserializer: JsonDeserializer<VALUE>): BodyLensSpec<VALUE> = map(deserializer::deserialize)