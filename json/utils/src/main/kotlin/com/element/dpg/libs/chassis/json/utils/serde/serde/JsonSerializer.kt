package com.element.dpg.libs.chassis.json.utils.serde.serde

import org.json.JSONObject

fun interface JsonSerializer<in VALUE : Any> {

    fun serialize(value: VALUE): JSONObject
}