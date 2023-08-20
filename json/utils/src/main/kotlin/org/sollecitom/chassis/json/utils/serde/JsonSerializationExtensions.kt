package org.sollecitom.chassis.json.utils.serde

import org.json.JSONObject
import org.sollecitom.chassis.json.utils.getJSONObjectOrNull
import org.sollecitom.chassis.json.utils.getRequiredJSONObject

fun <VALUE : Any> JSONObject.getValueOrNull(key: String, deserializer: JsonDeserializer<VALUE>): VALUE? = getJSONObjectOrNull(key)?.let(deserializer::deserialize)

fun <VALUE : Any> JSONObject.getValue(key: String, deserializer: JsonDeserializer<VALUE>): VALUE = getRequiredJSONObject(key).let(deserializer::deserialize)