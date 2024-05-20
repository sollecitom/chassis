package com.element.dpg.libs.chassis.json.utils.serde

import org.json.JSONObject

fun interface JsonDeserializer<out VALUE : Any> {

    fun deserialize(json: JSONObject): VALUE
}