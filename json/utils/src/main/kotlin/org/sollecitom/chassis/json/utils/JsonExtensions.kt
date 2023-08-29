package org.sollecitom.chassis.json.utils

import kotlinx.datetime.Instant
import org.json.JSONArray
import org.json.JSONObject

fun JSONObject.getLongOrNull(field: String): Long? = runCatching { getLong(field) }.getOrNull()

fun JSONObject.getStringOrNull(field: String): String? = runCatching { getString(field) }.getOrNull()

fun JSONObject.getJSONObjectOrNull(field: String): JSONObject? = runCatching { getJSONObject(field) }.getOrNull()

fun JSONObject.getIntOrNull(field: String): Int? = runCatching { getInt(field) }.getOrNull()

fun JSONObject.getJSONArrayOrNull(field: String): JSONArray? = runCatching { getJSONArray(field) }.getOrNull()

fun JSONArray.getItem(index: Int): JSONObject? = runCatching { get(index) as JSONObject }.getOrNull()

fun JSONObject.getRequiredString(key: String): String = getString(key)!!

fun JSONObject.getRequiredJSONObject(key: String): JSONObject = getJSONObject(key)!!

fun JSONObject.getRequiredJSONArray(key: String): JSONArray = getJSONArray(key)!!

fun JSONObject.deepEquals(other: JSONObject) = toMap() == other.toMap()

fun JSONObject.deepHashCode() = toMap().hashCode()

fun JSONArray.deepEquals(other: JSONArray) = toList() == other.toList()

fun JSONArray.deepHashCode() = toList().hashCode()

fun JSONObject.getInstantOrNull(key: String) = getStringOrNull(key)?.let(Instant.Companion::parse)
fun JSONObject.getRequiredInstant(key: String) = getInstantOrNull(key)!!
