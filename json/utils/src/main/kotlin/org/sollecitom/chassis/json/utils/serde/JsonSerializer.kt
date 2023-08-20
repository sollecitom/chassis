package org.sollecitom.chassis.json.utils.serde

import org.json.JSONObject

fun interface JsonSerializer<in VALUE : Any> {

    fun serialize(value: VALUE): JSONObject
}