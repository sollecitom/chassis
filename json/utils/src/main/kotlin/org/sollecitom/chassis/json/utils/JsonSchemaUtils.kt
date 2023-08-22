package org.sollecitom.chassis.json.utils

import com.github.erosb.jsonsKema.*
import org.json.JSONArray
import org.json.JSONObject
import org.sollecitom.chassis.resource.utils.ResourceLoader.openAsStream

//TODO write tests

fun jsonSchemaAt(location: String): Schema = openAsStream(location).use {

    val schemaJson = JsonParser(it).parse()
    SchemaLoader(schemaJson).load()
}

fun Schema.validate(json: IJsonValue) = Validator.forSchema(this).validate(json)

fun Schema.validate(json: JSONObject) = validate(JsonParser(json.toString()).parse())

fun Schema.validate(json: JSONArray) = validate(JsonParser(json.toString()).parse())