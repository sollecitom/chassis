package com.element.dpg.libs.chassis.json.utils.serde

import com.element.dpg.libs.chassis.json.utils.getJSONObjectOrNull
import com.element.dpg.libs.chassis.json.utils.getRequiredJSONArray
import com.element.dpg.libs.chassis.json.utils.getRequiredJSONObject
import org.json.JSONArray
import org.json.JSONObject

fun <VALUE : Any> JSONObject.getValueOrNull(key: String, deserializer: JsonDeserializer<VALUE>): VALUE? = getJSONObjectOrNull(key)?.let(deserializer::deserialize)

fun <VALUE : Any> JSONObject.getValue(key: String, deserializer: JsonDeserializer<VALUE>): VALUE = getRequiredJSONObject(key).let(deserializer::deserialize)

fun <VALUE : Any> JSONObject.setValue(key: String, value: VALUE, serializer: JsonSerializer<VALUE>): JSONObject = put(key, serializer.serialize(value))

fun <VALUE : Any> JSONObject.setValues(key: String, values: Iterable<VALUE>, serializer: JsonSerializer<VALUE>): JSONObject = put(key, values.map(serializer::serialize).fold(JSONArray(), JSONArray::put))

fun <VALUE : Any> JSONObject.getValues(key: String, deserializer: JsonDeserializer<VALUE>): List<VALUE> = getRequiredJSONArray(key).map { it as JSONObject }.map(deserializer::deserialize)

fun <VALUE : Any> JSONObject.setValueOrNull(key: String, value: VALUE?, serializer: JsonSerializer<VALUE>): JSONObject = put(key, value?.let { serializer.serialize(it) })